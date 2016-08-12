package com.joany.stickylistheaders.utils;

import java.util.Comparator;

/**
 * Created by joany on 2016/8/11
 */
public class PinYinComparator implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        String string1 = (String) o1;
        String string2 = (String) o2;
        String divider1 = "";
        String divider2="";
        if(string1 != null) {
            divider1 = PinYinUtil.convertToFirstSpell(string1).substring(0,1)
                    .toUpperCase();
        }

        if(string2 != null) {
            divider2 = PinYinUtil.convertToFirstSpell(string2).substring(0,1)
                    .toUpperCase();
        }
        return divider1.compareTo(divider2);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }
}
