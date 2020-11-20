package com.serosoft.academiassu.Modules.Event.Models;

import java.io.Serializable;
import java.util.Comparator;

public class Events_Dto implements Serializable {

    int id,event_id,type;
    long start_long,end_long;
    String start,end,title,notes,eventBanner,eventVenue,conductedBy,eventName;
    Boolean isCompleteDay, isRecurring;

    public Events_Dto(int id, int event_id, int type, long start_long, long end_long, String start, String end, String title, String notes, String eventBanner, String eventVenue, String conductedBy, String eventName, Boolean isCompleteDay, Boolean isRecurring) {
        this.id = id;
        this.event_id = event_id;
        this.type = type;
        this.start_long = start_long;
        this.end_long = end_long;
        this.start = start;
        this.end = end;
        this.title = title;
        this.notes = notes;
        this.eventBanner = eventBanner;
        this.eventVenue = eventVenue;
        this.conductedBy = conductedBy;
        this.eventName = eventName;
        this.isCompleteDay = isCompleteDay;
        this.isRecurring = isRecurring;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getStart_long() {
        return start_long;
    }

    public void setStart_long(long start_long) {
        this.start_long = start_long;
    }

    public long getEnd_long() {
        return end_long;
    }

    public void setEnd_long(long end_long) {
        this.end_long = end_long;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getEventBanner() {
        return eventBanner;
    }

    public void setEventBanner(String eventBanner) {
        this.eventBanner = eventBanner;
    }

    public String getEventVenue() {
        return eventVenue;
    }

    public void setEventVenue(String eventVenue) {
        this.eventVenue = eventVenue;
    }

    public String getConductedBy() {
        return conductedBy;
    }

    public void setConductedBy(String conductedBy) {
        this.conductedBy = conductedBy;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Boolean getIsCompleteDay() {
        return isCompleteDay;
    }

    public void setIsCompleteDay(Boolean isCompleteDay) {
        this.isCompleteDay = isCompleteDay;
    }

    public Boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(Boolean isRecurring) {
        this.isRecurring = isRecurring;
    }

    public static Comparator<Events_Dto> sortMostRecently = new Comparator<Events_Dto>()
    {
        @Override
        public int compare(Events_Dto m1, Events_Dto m2)
        {
            Long id1 =  m1.getStart_long();
            Long id2 =  m2.getStart_long();

            return id1.compareTo(id2);
        }
    };

    public static Comparator<Events_Dto> sortMostCurrent = new Comparator<Events_Dto>()
    {
        @Override
        public int compare(Events_Dto m1, Events_Dto m2)
        {
            Long id1 =  m1.getStart_long();
            Long id2 =  m2.getStart_long();

            return id1.compareTo(id2);
        }
    };

    public static Comparator<Events_Dto> sortMostPassed = new Comparator<Events_Dto>()
    {
        @Override
        public int compare(Events_Dto m1, Events_Dto m2)
        {
            Long id1 =  m1.getEnd_long();
            Long id2 =  m2.getEnd_long();

            return id2.compareTo(id1);
        }
    };
}
