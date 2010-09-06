/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.doclava;

import com.google.clearsilver.jsilver.data.Data;

import com.google.common.collect.ImmutableList;
import java.util.*;

public class TypeInfo {
  public static final Set<String> PRIMITIVE_TYPES = Collections.unmodifiableSet(
      new HashSet<String>(Arrays.asList("boolean", "byte", "char", "double", "float", "int",
      "long", "short", "void")));
  
  public TypeInfo(boolean isPrimitive, String dimension, String simpleTypeName,
      String qualifiedTypeName, ClassInfo cl) {
    mIsPrimitive = isPrimitive;
    mDimension = dimension;
    mSimpleTypeName = simpleTypeName;
    mQualifiedTypeName = qualifiedTypeName;
    mClass = cl;
  }

  public TypeInfo(String typeString) {
    // VarArgs
    if (typeString.endsWith("...")) {
      typeString = typeString.substring(0, typeString.length() - 3);
    }
    
    // Generic parameters
    int paramStartPos = typeString.indexOf('<');
    if (paramStartPos > -1) {
      ArrayList<TypeInfo> generics = new ArrayList<TypeInfo>();
      int paramEndPos = typeString.lastIndexOf('>');
      
      int entryStartPos = paramStartPos + 1;
      int bracketNesting = 0;
      for (int i = entryStartPos; i < paramEndPos; i++) {
        char c = typeString.charAt(i);
        if (c == ',' && bracketNesting == 0) {
          String entry = typeString.substring(entryStartPos, i).trim();
          TypeInfo info = new TypeInfo(entry);
          generics.add(info);
          entryStartPos = i + 1;
        } else if (c == '<') {
          bracketNesting++;
        } else if (c == '>') {
          bracketNesting--;
        }
      }
     
      TypeInfo info = new TypeInfo(typeString.substring(entryStartPos, paramEndPos).trim());
      generics.add(info);
      
      mTypeArguments = generics;

      if (paramEndPos < typeString.length() - 1) {
        typeString = typeString.substring(0,paramStartPos) + typeString.substring(paramEndPos + 1);
      } else {
        typeString = typeString.substring(0,paramStartPos);
      }
    }
    
    // Dimensions
    int pos = typeString.indexOf('['); 
    if (pos > -1) {
      mDimension = typeString.substring(pos);
      typeString = typeString.substring(0, pos);
    } else {
      mDimension = "";
    }
   
    if (PRIMITIVE_TYPES.contains(typeString)) {
      mIsPrimitive = true;
      mSimpleTypeName = typeString;
      mQualifiedTypeName = typeString;
    } else {
      mQualifiedTypeName = typeString;
      pos = typeString.lastIndexOf('.');
      if (pos > -1) {
        mSimpleTypeName = typeString.substring(pos + 1);
      } else {
        mSimpleTypeName = typeString;
      }
    }
  }

  public ClassInfo asClassInfo() {
    return mClass;
  }

  public boolean isPrimitive() {
    return mIsPrimitive;
  }

  public String dimension() {
    return mDimension;
  }

  public String simpleTypeName() {
    return mSimpleTypeName;
  }

  public String qualifiedTypeName() {
    return mQualifiedTypeName;
  }

  public String fullName() {
    if (mFullName != null) {
      return mFullName;
    } else {
      return fullName(new HashSet<String>());
    }
  }

  public static String typeArgumentsName(List<TypeInfo> args, HashSet<String> typeVars) {
    String result = "<";
    for (int i = 0; i < args.size(); i++) {
      result += args.get(i).fullName(typeVars);
      if (i != args.size() - 1) {
        result += ", ";
      }
    }
    result += ">";
    return result;
  }

  public String fullName(HashSet<String> typeVars) {
    mFullName = fullNameNoDimension(typeVars) + mDimension;
    return mFullName;
  }

  public String fullNameNoDimension(HashSet<String> typeVars) {
    String fullName;
    if (mIsTypeVariable) {
      if (typeVars.contains(mQualifiedTypeName)) {
        // don't recurse forever with the parameters. This handles
        // Enum<K extends Enum<K>>
        return mQualifiedTypeName;
      }
      typeVars.add(mQualifiedTypeName);
    }
    /*
     * if (fullName != null) { return fullName; }
     */
    fullName = mQualifiedTypeName;
    if (mTypeArguments != null && !mTypeArguments.isEmpty()) {
      fullName += typeArgumentsName(mTypeArguments, typeVars);
    } else if (mSuperBounds != null && !mSuperBounds.isEmpty()) {
      fullName += " super " + mSuperBounds.get(0).fullName(typeVars);
      for (int i = 1; i < mSuperBounds.size(); i++) {
        fullName += " & " + mSuperBounds.get(i).fullName(typeVars);
      }
    } else if (mExtendsBounds != null && !mExtendsBounds.isEmpty()) {
      fullName += " extends " + mExtendsBounds.get(0).fullName(typeVars);
      for (int i = 1; i < mExtendsBounds.size(); i++) {
        fullName += " & " + mExtendsBounds.get(i).fullName(typeVars);
      }
    }
    return fullName;
  }

  public List<TypeInfo> typeArguments() {
    return mTypeArguments;
  }

  public void makeHDF(Data data, String base) {
    makeHDFRecursive(data, base, false, false, new HashSet<String>());
  }

  public void makeQualifiedHDF(Data data, String base) {
    makeHDFRecursive(data, base, true, false, new HashSet<String>());
  }

  public void makeHDF(Data data, String base, boolean isLastVararg, HashSet<String> typeVariables) {
    makeHDFRecursive(data, base, false, isLastVararg, typeVariables);
  }

  public void makeQualifiedHDF(Data data, String base, HashSet<String> typeVariables) {
    makeHDFRecursive(data, base, true, false, typeVariables);
  }

  private void makeHDFRecursive(Data data, String base, boolean qualified, boolean isLastVararg,
      HashSet<String> typeVars) {
    String label = qualified ? qualifiedTypeName() : simpleTypeName();
    label += (isLastVararg) ? "..." : dimension();
    data.setValue(base + ".label", label);
    if (mIsTypeVariable || mIsWildcard) {
      // could link to an @param tag on the class to describe this
      // but for now, just don't make it a link
    } else if (!isPrimitive() && mClass != null) {
      if (mClass.isDefinedLocally()) {
        data.setValue(base + ".link", mClass.htmlPage());
        data.setValue(base + ".since.key", SinceTagger.keyForName(mClass.getSince()));
        data.setValue(base + ".since.name", mClass.getSince());
      } else {
        Doclava.federationTagger.tagAll(ImmutableList.of(mClass));
        if (!mClass.getFederatedReferences().isEmpty()) {
          FederatedSite site = mClass.getFederatedReferences().iterator().next();
          data.setValue(base + ".link", site.linkFor(mClass.relativePath()));
          data.setValue(base + ".federated", site.name());
        }
      }
    }

    if (mIsTypeVariable) {
      if (typeVars.contains(qualifiedTypeName())) {
        // don't recurse forever with the parameters. This handles
        // Enum<K extends Enum<K>>
        return;
      }
      typeVars.add(qualifiedTypeName());
    }
    if (mTypeArguments != null) {
      TypeInfo.makeHDF(data, base + ".typeArguments", mTypeArguments, qualified, typeVars);
    }
    if (mSuperBounds != null) {
      TypeInfo.makeHDF(data, base + ".superBounds", mSuperBounds, qualified, typeVars);
    }
    if (mExtendsBounds != null) {
      TypeInfo.makeHDF(data, base + ".extendsBounds", mExtendsBounds, qualified, typeVars);
    }
  }

  public static void makeHDF(Data data, String base, List<TypeInfo> types, boolean qualified,
      HashSet<String> typeVariables) {
    int i = 0;
    for (TypeInfo typeInfo : types) {
      typeInfo.makeHDFRecursive(data, base + "." + i, qualified, false, typeVariables);
      i++;
    }
  }

  public static void makeHDF(Data data, String base, List<TypeInfo> types, boolean qualified) {
    makeHDF(data, base, types, qualified, new HashSet<String>());
  }

  void setTypeArguments(List<TypeInfo> args) {
    mTypeArguments = args;
  }

  void setBounds(List<TypeInfo> superBounds, List<TypeInfo> extendsBounds) {
    mSuperBounds = superBounds;
    mExtendsBounds = extendsBounds;
  }

  void setIsTypeVariable(boolean b) {
    mIsTypeVariable = b;
  }

  void setIsWildcard(boolean b) {
    mIsWildcard = b;
  }

  static HashSet<String> typeVariables(List<TypeInfo> params) {
    return typeVariables(params, new HashSet<String>());
  }

  static HashSet<String> typeVariables(List<TypeInfo> params, HashSet<String> result) {
    for (TypeInfo t : params) {
      if (t.mIsTypeVariable) {
        result.add(t.mQualifiedTypeName);
      }
    }
    return result;
  }


  public boolean isTypeVariable() {
    return mIsTypeVariable;
  }

  public String defaultValue() {
    if (mIsPrimitive) {
      if ("boolean".equals(mSimpleTypeName)) {
        return "false";
      } else {
        return "0";
      }
    } else {
      return "null";
    }
  }

  @Override
  public String toString() {
    String returnString = "";
    returnString +=
        "Primitive?: " + mIsPrimitive + " TypeVariable?: " + mIsTypeVariable + " Wildcard?: "
            + mIsWildcard + " Dimension: " + mDimension + " QualifedTypeName: "
            + mQualifiedTypeName;

    if (mTypeArguments != null) {
      returnString += "\nTypeArguments: ";
      for (TypeInfo tA : mTypeArguments) {
        returnString += tA.qualifiedTypeName() + "(" + tA + ") ";
      }
    }
    if (mSuperBounds != null) {
      returnString += "\nSuperBounds: ";
      for (TypeInfo tA : mSuperBounds) {
        returnString += tA.qualifiedTypeName() + "(" + tA + ") ";
      }
    }
    if (mExtendsBounds != null) {
      returnString += "\nExtendsBounds: ";
      for (TypeInfo tA : mExtendsBounds) {
        returnString += tA.qualifiedTypeName() + "(" + tA + ") ";
      }
    }
    return returnString;
  }

  private boolean mIsPrimitive;
  private boolean mIsTypeVariable;
  private boolean mIsWildcard;
  private String mDimension;
  private String mSimpleTypeName;
  private String mQualifiedTypeName;
  private ClassInfo mClass;
  private List<TypeInfo> mTypeArguments;
  private List<TypeInfo> mSuperBounds;
  private List<TypeInfo> mExtendsBounds;
  private String mFullName;
}
