package org.exoplatform.task.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author <a href="trongtt@gmail.com">Trong Tran</a>
 * @version $Revision$
 */
public class Project {
    private String id;

    private String name;

    private String owner;

    private String desc;

    //user have permission can edit task in this project
    //but only owner can edit project info, or delete project
    private Set<String> memberships = new HashSet<String>();

    //We should use UTC time for this
    private long dateCreated;

    public Project() {
        this(null, null, null);
    }
    
    public Project(String owner, String name, String desc) {
        this.name = name;
        this.owner = owner;
        this.desc = desc;
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Set<String> getMemberships() {        
        return Collections.unmodifiableSet(memberships);
    }

    public void setMemberships(Set<String> memberships) {
        this.memberships = memberships;
    }

    public long getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(long dateCreated) {
        this.dateCreated = dateCreated;
    }
    
}