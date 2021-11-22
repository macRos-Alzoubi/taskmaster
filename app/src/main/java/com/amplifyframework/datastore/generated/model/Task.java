package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks")
@Index(name = "byTeam", fields = {"teamId"})
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField TITLE = field("Task", "title");
  public static final QueryField DESCRIPTION = field("Task", "description");
  public static final QueryField STATUS = field("Task", "status");
  public static final QueryField TEAM_ID = field("Task", "teamId");
  public static final QueryField IMG_URL = field("Task", "imgUrl");
  public static final QueryField LAT = field("Task", "lat");
  public static final QueryField LON = field("Task", "lon");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String description;
  private final @ModelField(targetType="String") String status;
  private final @ModelField(targetType="ID", isRequired = true) String teamId;
  private final @ModelField(targetType="String") String imgUrl;
  private final @ModelField(targetType="Float") Double lat;
  private final @ModelField(targetType="Float") Double lon;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getDescription() {
      return description;
  }
  
  public String getStatus() {
      return status;
  }
  
  public String getTeamId() {
      return teamId;
  }
  
  public String getImgUrl() {
      return imgUrl;
  }
  
  public Double getLat() {
      return lat;
  }
  
  public Double getLon() {
      return lon;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  private Task(String id, String title, String description, String status, String teamId, String imgUrl, Double lat, Double lon) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.teamId = teamId;
    this.imgUrl = imgUrl;
    this.lat = lat;
    this.lon = lon;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getDescription(), task.getDescription()) &&
              ObjectsCompat.equals(getStatus(), task.getStatus()) &&
              ObjectsCompat.equals(getTeamId(), task.getTeamId()) &&
              ObjectsCompat.equals(getImgUrl(), task.getImgUrl()) &&
              ObjectsCompat.equals(getLat(), task.getLat()) &&
              ObjectsCompat.equals(getLon(), task.getLon()) &&
              ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getDescription())
      .append(getStatus())
      .append(getTeamId())
      .append(getImgUrl())
      .append(getLat())
      .append(getLon())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("status=" + String.valueOf(getStatus()) + ", ")
      .append("teamId=" + String.valueOf(getTeamId()) + ", ")
      .append("imgUrl=" + String.valueOf(getImgUrl()) + ", ")
      .append("lat=" + String.valueOf(getLat()) + ", ")
      .append("lon=" + String.valueOf(getLon()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      description,
      status,
      teamId,
      imgUrl,
      lat,
      lon);
  }
  public interface TitleStep {
    TeamIdStep title(String title);
  }
  

  public interface TeamIdStep {
    BuildStep teamId(String teamId);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep description(String description);
    BuildStep status(String status);
    BuildStep imgUrl(String imgUrl);
    BuildStep lat(Double lat);
    BuildStep lon(Double lon);
  }
  

  public static class Builder implements TitleStep, TeamIdStep, BuildStep {
    private String id;
    private String title;
    private String teamId;
    private String description;
    private String status;
    private String imgUrl;
    private Double lat;
    private Double lon;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          title,
          description,
          status,
          teamId,
          imgUrl,
          lat,
          lon);
    }
    
    @Override
     public TeamIdStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep teamId(String teamId) {
        Objects.requireNonNull(teamId);
        this.teamId = teamId;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep status(String status) {
        this.status = status;
        return this;
    }
    
    @Override
     public BuildStep imgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }
    
    @Override
     public BuildStep lat(Double lat) {
        this.lat = lat;
        return this;
    }
    
    @Override
     public BuildStep lon(Double lon) {
        this.lon = lon;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String description, String status, String teamId, String imgUrl, Double lat, Double lon) {
      super.id(id);
      super.title(title)
        .teamId(teamId)
        .description(description)
        .status(status)
        .imgUrl(imgUrl)
        .lat(lat)
        .lon(lon);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder teamId(String teamId) {
      return (CopyOfBuilder) super.teamId(teamId);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder status(String status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder imgUrl(String imgUrl) {
      return (CopyOfBuilder) super.imgUrl(imgUrl);
    }
    
    @Override
     public CopyOfBuilder lat(Double lat) {
      return (CopyOfBuilder) super.lat(lat);
    }
    
    @Override
     public CopyOfBuilder lon(Double lon) {
      return (CopyOfBuilder) super.lon(lon);
    }
  }
  
}
