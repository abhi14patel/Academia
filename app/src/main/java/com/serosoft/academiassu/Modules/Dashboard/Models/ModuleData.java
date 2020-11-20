package com.serosoft.academiassu.Modules.Dashboard.Models;

import java.util.Comparator;

/**
 * Created by Abhishek on October 14 2019.
 */

public class ModuleData
{
   private int id;
   private int color;
   private int icon;
   private String name;

   public ModuleData()
   {}

   public ModuleData(int id, int color, int icon, String name)
   {
      this.id = id;
      this.color = color;
      this.icon = icon;
      this.name = name;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getColor() {
      return color;
   }

   public void setColor(int color) {
      this.color = color;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getIcon() {
      return icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public static Comparator<ModuleData> sortModuleId = new Comparator<ModuleData>()
   {
      @Override
      public int compare(ModuleData m1, ModuleData m2)
      {
         int id1 = m1.getId();
         int id2 = m2.getId();

         return id1-id2;
      }
   };
}
