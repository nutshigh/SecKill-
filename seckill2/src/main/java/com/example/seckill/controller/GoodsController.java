package com.example.seckill.controller;

import com.example.seckill.pojo.TUser;
import com.example.seckill.service.IGoodsService;
import com.example.seckill.service.ITUserService;
import com.example.seckill.vo.DetailVo;
import com.example.seckill.vo.GoodsVo;
import com.example.seckill.vo.RespBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private ITUserService userService;

    @Autowired
    private IGoodsService goodsService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    @RequestMapping(value = "/toList", produces = "text/html;charset=utf-8")
    @ResponseBody
    public String toList(Model model, TUser user, HttpServletRequest request,HttpServletResponse response){
//        if(StringUtils.isEmpty(ticket)){
//            return "login";
//        }
//        TUser user = (TUser) session.getAttribute(ticket);
//        TUser user = userService.getUserByCookie(ticket,request,response);
//        页面缓存：直接跳转会使得每一次都重新渲染前端页面，因此可以把页面缓存起来到数据库，减少渲染时间
        ValueOperations valueOperations = redisTemplate.opsForValue();
        String html = (String) valueOperations.get("goodsList");
        if(!StringUtils.isEmpty(html)){
            return html;
        }
        model.addAttribute("user",user);
        model.addAttribute("goodsList",goodsService.findGoodVo());
        //手动渲染
        WebContext context = new WebContext(request, response, request.getServletContext(),request.getLocale(),model.asMap());
        html = thymeleafViewResolver.getTemplateEngine().process("goodsList",context);
        if(!StringUtils.isEmpty(html)){
            valueOperations.set("goodsList",html,1,TimeUnit.MINUTES);
        }
        return html;
    }

//    @RequestMapping(value = "/toDetail/goodId={goodsId}", produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String goodsDetail(Model model,TUser user, @PathVariable Long goodsId, HttpServletRequest request,
//                              HttpServletResponse response){
//        ValueOperations valueOperations = redisTemplate.opsForValue();
//        String goodsDetailhtml = (String) valueOperations.get("goodsDetailhtml" + goodsId);
//        if(goodsDetailhtml != null){
//            return goodsDetailhtml;
//        }
//        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
//        Date startDate = goodsVo.getStartDate();
//        Date endDate = goodsVo.getEndDate();
//        Date nowDate = new Date();
//        int seckillStatus = 0;
//        int remainSeconds = 0;
//        if(nowDate.before(startDate)){
//            remainSeconds = (int)((startDate.getTime() - nowDate.getTime()) / 1000);
//        }else if(nowDate.after(endDate)){
//            seckillStatus = 2;
//            remainSeconds = -1;
//        }else{
//            seckillStatus = 2;
//            remainSeconds = 0;
//        }
//        model.addAttribute("remainSeconds",remainSeconds);
//        model.addAttribute("seckillStatus",seckillStatus);
//        model.addAttribute("user",user);
//        model.addAttribute("goods",goodsService.findGoodVoByGoodsId(goodsId));
//        WebContext webContext = new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap());
//        //goodsDetail页面名称
//        goodsDetailhtml = thymeleafViewResolver.getTemplateEngine().process("goodsDetail", webContext);
//        if (!StringUtils.isEmpty(goodsDetailhtml)){
//            valueOperations.set("goodsDetailhtml",+goodsId,60,TimeUnit.SECONDS);
//        }
//        return goodsDetailhtml;
//    }

    @RequestMapping("toDetail/{goodsId}")
    @ResponseBody
    public RespBean toDetail(Model model, TUser user, @PathVariable Long goodsId){
        GoodsVo goodsVo = goodsService.findGoodVoByGoodsId(goodsId);
        Date startDate = goodsVo.getStartDate();
        Date endDate = goodsVo.getEndDate();
        Date nowDate = new Date();
        //秒杀状态
        int seckillStatus = 0;
        //秒杀倒计时
        int remainSeconds = 0;
        //秒杀还未开始
        if (nowDate.before(startDate)){
            //seckillStatus还是0 不处理seckillStatus
            remainSeconds = (int)((startDate.getTime()-nowDate.getTime())/1000);

        }else if (nowDate.after(endDate)){
            //秒杀结束
            seckillStatus = 2;
            remainSeconds  = -1;
        }else {
            //秒杀进行中
            seckillStatus = 1;
            remainSeconds = 0;
        }

        DetailVo detailVo = new DetailVo();
        detailVo.setUser(user);
        detailVo.setGoodsVo(goodsVo);
        detailVo.setSecKillStatus(seckillStatus);
        detailVo.setRemainSeconds(remainSeconds);
        return RespBean.success(detailVo);
    }
}
