//package com.lp.demo.spring.event;
//
//import com.lp.demo.common.util.ConsoleColorUtil;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
///**
// * @author lp
// * @date 2022/10/12 15:48
// * @desc
// **/
//@Component
//public class UserInfoEventListener<T> implements EventServiceListener<T> {
//
//    @Override
//    public void onApplicationEvent(Event<T> event) {
//        ConsoleColorUtil.printDefaultColor(">>>>>>>UserInfoEventListener");
//        event.execute(event.getData());
//    }
//
//
////    @Override
////    @EventListener
////    public void onApplicationEvent(UserInfoEvent userInfoEvent) {
////        ConsoleColorUtil.printDefaultColor(">>>>>>>UserInfoEventListener");
////        userInfoEvent.execute(userInfoEvent.getData());
////    }
//}
