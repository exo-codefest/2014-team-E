package org.exoplatform.task;

import java.util.Collections;
import java.util.List;

public class Utils {
    public static <T> List<T> subList(List<T> it, int offset, int limit) {    
        if (it == null) {
          return it;
        }
        if (it.size() <= offset) {
          return Collections.emptyList();
        }
        if (limit < 0) {
          limit = it.size();
        }
        if (offset < 0) {
          offset = 0;
        }    
        limit = offset + limit > it.size() ? it.size() - offset : limit;    
        return (limit == it.size() && offset == 0) ? it : it.subList(offset, offset + limit);
    }
    
    public static String queryEscape(String s) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '%' || ch == '"' || ch == '_' || ch == '\\') {
                buffer.append('\\').append(ch);
            } else if (ch == '\'') {
                buffer.append("''");
            } else {
                buffer.append(ch);
            }
        }
        return buffer.toString();
    }
}
