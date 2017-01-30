package com.valyakinaleksey.roleplayingsystem.modules.gamescreen.domain.model;

import com.ezhome.rxfirebase2.FirebaseChildEvent;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;
import com.valyakinaleksey.roleplayingsystem.modules.auth.domain.model.User;
import com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.valyakinaleksey.roleplayingsystem.utils.FireBaseUtils.UID;

public class UserInGameModel implements Serializable {
  public static final String FIELD_NAME = "name";

  private String uid;
  private String name;

  @Exclude private User user;
  @Exclude private FirebaseChildEvent.EventType eventType;

  public UserInGameModel() {
  }

  public UserInGameModel(String uid, String name) {
    this.uid = uid;
    this.name = name;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Exclude public FirebaseChildEvent.EventType getEventType() {
    return eventType;
  }

  @Exclude public void setEventType(FirebaseChildEvent.EventType eventType) {
    this.eventType = eventType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getUid() {
    return uid;
  }

  public void setUid(String uid) {
    this.uid = uid;
  }

  @Exclude public Map<String, Object> toMap() {
    HashMap<String, Object> result = new LinkedHashMap<>();
    result.put(UID, uid);
    result.put(FIELD_NAME, name);
    return result;
  }
}
      