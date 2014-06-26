package org.exoplatform.task.model;

import java.util.Set;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Project {
    public String name;

    public String desc;

    public Set<String> memberships;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Set<String> getMemberships() {
        return memberships;
    }

    public void setMemberships(Set<String> memberships) {
        this.memberships = memberships;
    }

    public Project(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }
}