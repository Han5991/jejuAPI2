package com.jeju.jejuapi2.response;

import com.fasterxml.jackson.core.JacksonException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Main {
    public static void main(String[] args) throws JacksonException {
//        SkillResponse result = new SkillResponse();
//        result.addBasicCard(new BasicCardBuilder().title("카드입니다.").thumbnail(new ThumbnailBuilder().imageUrl("url").build()).build());
//        System.out.println(result.getSkillPayload().toString());
//
//        SkillResponse result2 = new SkillResponse();
//        result2.addCommerceCard(new CommerceCardBuilder()
//                .description("설명입니다.")
//                .price(10000)
//                .thumbnails(new ThumbnailBuilder()
//                        .imageUrl("imageurl")
//                        .build())
//                .buttons(new ButtonBuilder()
//                        .label("버튼입니다.")
//                        .action(EnumResponseType.ButtonActionType.MESSAGE.getTypeText())
//                        .messageText("메시지입니다.")
//                        .build())
//                .profile(new ProfileBuilder()
//                        .nickname("knight7024")
//                        .build())
//                .build());
//        System.out.println(result2.getSkillPayload().toString());
        SkillResponse result = new SkillResponse();
        result.addCommerceCard(new CommerceCardBuilder()
                .description("따끈따끈한 보물 상자 팝니다")
                .price(10000)
                .discount(1000)
                .thumbnails(new ThumbnailBuilder()
                        .imageUrl("http://k.kakaocdn.net/dn/83BvP/bl20duRC1Q1/lj3JUcmrzC53YIjNDkqbWK/i_6piz1p.jpg")
                        .link(new LinkBuilder()
                                .web("https://store.kakaofriends.com/kr/products/1542")
                                .build())
                        .build())
                .profile(new ProfileBuilder()
                        .imageUrl("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT4BJ9LU4Ikr_EvZLmijfcjzQKMRCJ2bO3A8SVKNuQ78zu2KOqM")
                        .nickname("보물상자 팝니다")
                        .build())
                .buttons(new ButtonBuilder()
                        .label("구매하기")
                        .action(EnumResponseType.ButtonActionType.WEBLINK.getTypeText())
                        .webLinkUrl("https://store.kakaofriends.com/kr/products/1542")
                        .build())
                .buttons(new ButtonBuilder()
                        .label("전화하기")
                        .action(EnumResponseType.ButtonActionType.PHONE.getTypeText())
                        .phoneNumber("354-86-00070")
                        .build())
                .buttons(new ButtonBuilder()
                        .label("공유하기")
                        .action(EnumResponseType.ButtonActionType.SHARE.getTypeText())
                        .build())
                .build());
        System.out.println(result.getSkillPayload().toString());
    }
}