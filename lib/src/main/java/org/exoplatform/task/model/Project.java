package org.exoplatform.task.model;

import java.util.Set;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Project {
    public String id;

    public String name;

    public String desc;

    public Set<String> memberships;

    public Project(String id, String name, String desc) {
        this.id = id;
        this.name = name;
        this.desc = desc;
    }

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

    public String getId() {
        return id;
    }
}