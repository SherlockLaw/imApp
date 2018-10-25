package com.sherlock.imapp.constant;

import com.sherlock.imapp.entity.Sex;

/**
 * Created by Administrator on 2018/5/12 0012.
 */

public class OtherConstant {
    public static final Sex[] sexArray = new Sex[] {new Sex(1,"男"),new Sex(2,"女")};

    public static Sex getSex(int sex){
        for (int i=0; i<sexArray.length; ++i){
            Sex e = sexArray[i];
            if (sex==sexArray[i].getSex()) {
                return e;
            }
        }
        return null;
    }
    public static int getSexIndex(int sex) {
        for (int i=0; i<sexArray.length; ++i){
            Sex e = sexArray[i];
            if (sex==sexArray[i].getSex()) {
                return i;
            }
        }
        return -1;
    }
    public enum IsFriendEnum {
        notFriend(1,"非好友"),
        friend(2,"好友");

        private int index;
        private String text;

        IsFriendEnum(int sex, String text){
            this.index = sex;
            this.text = text;
        }

        public static IsFriendEnum getEnum(int index) {
            IsFriendEnum[] l = IsFriendEnum.values();
            for (IsFriendEnum e: l){
                if (e.index==index) {
                    return e;
                }
            }
            return null;
        }

        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }
    }
}
