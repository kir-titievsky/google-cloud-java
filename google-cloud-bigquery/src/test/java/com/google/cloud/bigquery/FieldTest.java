/*
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.cloud.bigquery;

import static org.junit.Assert.assertEquals;

import com.google.common.collect.ImmutableList;

import org.junit.Test;

public class FieldTest {

  private static final String FIELD_NAME1 = "StringField";
  private static final String FIELD_NAME2 = "IntegerField";
  private static final String FIELD_NAME3 = "RecordField";
  private static final Field.Type FIELD_TYPE1 = Field.Type.string();
  private static final Field.Type FIELD_TYPE2 = Field.Type.integer();
  private static final Field.Mode FIELD_MODE1 = Field.Mode.NULLABLE;
  private static final Field.Mode FIELD_MODE2 = Field.Mode.REPEATED;
  private static final Field.Mode FIELD_MODE3 = Field.Mode.REQUIRED;
  private static final String FIELD_DESCRIPTION1 = "FieldDescription1";
  private static final String FIELD_DESCRIPTION2 = "FieldDescription2";
  private static final String FIELD_DESCRIPTION3 = "FieldDescription3";
  private static final Field FIELD_SCHEMA1 = Field.newBuilder(FIELD_NAME1, FIELD_TYPE1)
      .setMode(FIELD_MODE1)
      .setDescription(FIELD_DESCRIPTION1)
      .build();
  private static final Field FIELD_SCHEMA2 = Field.newBuilder(FIELD_NAME2, FIELD_TYPE2)
      .setMode(FIELD_MODE2)
      .setDescription(FIELD_DESCRIPTION2)
      .build();
  private static final Field.Type FIELD_TYPE3 =
      Field.Type.record(ImmutableList.of(FIELD_SCHEMA1, FIELD_SCHEMA2));
  private static final Field FIELD_SCHEMA3 = Field
      .newBuilder(FIELD_NAME3, FIELD_TYPE3)
      .setMode(FIELD_MODE3)
      .setDescription(FIELD_DESCRIPTION3)
      .build();
  private static final Field DEPRECATED_FIELD_SCHEMA1 = Field.builder(FIELD_NAME1, FIELD_TYPE1)
      .mode(FIELD_MODE1)
      .description(FIELD_DESCRIPTION1)
      .build();
  private static final Field DEPRECATED_FIELD_SCHEMA2 = Field.builder(FIELD_NAME2, FIELD_TYPE2)
      .mode(FIELD_MODE2)
      .description(FIELD_DESCRIPTION2)
      .build();
  private static final Field DEPRECATED_FIELD_SCHEMA3 = Field
      .builder(FIELD_NAME3, FIELD_TYPE3)
      .mode(FIELD_MODE3)
      .description(FIELD_DESCRIPTION3)
      .build();

  @Test
  public void testToBuilder() {
    compareFieldSchemas(FIELD_SCHEMA1, FIELD_SCHEMA1.toBuilder().build());
    compareFieldSchemas(FIELD_SCHEMA2, FIELD_SCHEMA2.toBuilder().build());
    compareFieldSchemas(FIELD_SCHEMA3, FIELD_SCHEMA3.toBuilder().build());
    Field field = FIELD_SCHEMA1.toBuilder()
        .setDescription("New Description")
        .build();
    assertEquals("New Description", field.getDescription());
    field = field.toBuilder().setDescription(FIELD_DESCRIPTION1).build();
    compareFieldSchemas(FIELD_SCHEMA1, field);
  }

  @Test
  public void testToBuilderIncomplete() {
    Field field = Field.of(FIELD_NAME1, FIELD_TYPE1);
    compareFieldSchemas(field, field.toBuilder().build());
    field = Field.of(FIELD_NAME2, FIELD_TYPE3);
    compareFieldSchemas(field, field.toBuilder().build());
  }

  @Test
  public void testBuilder() {
    assertEquals(FIELD_NAME1, FIELD_SCHEMA1.getName());
    assertEquals(FIELD_TYPE1, FIELD_SCHEMA1.getType());
    assertEquals(FIELD_MODE1, FIELD_SCHEMA1.getMode());
    assertEquals(FIELD_DESCRIPTION1, FIELD_SCHEMA1.getDescription());
    assertEquals(null, FIELD_SCHEMA1.getFields());
    assertEquals(FIELD_NAME3, FIELD_SCHEMA3.getName());
    assertEquals(FIELD_TYPE3, FIELD_SCHEMA3.getType());
    assertEquals(FIELD_MODE3, FIELD_SCHEMA3.getMode());
    assertEquals(FIELD_DESCRIPTION3, FIELD_SCHEMA3.getDescription());
    assertEquals(ImmutableList.of(FIELD_SCHEMA1, FIELD_SCHEMA2), FIELD_SCHEMA3.getFields());
  }

  @Test
  public void testBuilderDeprecated() {
    assertEquals(FIELD_NAME1, DEPRECATED_FIELD_SCHEMA1.name());
    assertEquals(FIELD_TYPE1, DEPRECATED_FIELD_SCHEMA1.type());
    assertEquals(FIELD_MODE1, DEPRECATED_FIELD_SCHEMA1.mode());
    assertEquals(FIELD_DESCRIPTION1, DEPRECATED_FIELD_SCHEMA1.description());
    assertEquals(null, DEPRECATED_FIELD_SCHEMA1.fields());
    assertEquals(FIELD_NAME3, DEPRECATED_FIELD_SCHEMA3.name());
    assertEquals(FIELD_TYPE3, DEPRECATED_FIELD_SCHEMA3.type());
    assertEquals(FIELD_MODE3, DEPRECATED_FIELD_SCHEMA3.mode());
    assertEquals(FIELD_DESCRIPTION3, DEPRECATED_FIELD_SCHEMA3.description());
    assertEquals(ImmutableList.of(DEPRECATED_FIELD_SCHEMA1, DEPRECATED_FIELD_SCHEMA2),
        DEPRECATED_FIELD_SCHEMA3.fields());
  }

  @Test
  public void testToAndFromPb() {
    compareFieldSchemas(FIELD_SCHEMA1, Field.fromPb(FIELD_SCHEMA1.toPb()));
    compareFieldSchemas(FIELD_SCHEMA2, Field.fromPb(FIELD_SCHEMA2.toPb()));
    compareFieldSchemas(FIELD_SCHEMA3, Field.fromPb(FIELD_SCHEMA3.toPb()));
    Field field = Field.newBuilder(FIELD_NAME1, FIELD_TYPE1).build();
    compareFieldSchemas(field, Field.fromPb(field.toPb()));
  }

  private void compareFieldSchemas(Field expected, Field value) {
    assertEquals(expected, value);
    assertEquals(expected.getName(), value.getName());
    assertEquals(expected.getType(), value.getType());
    assertEquals(expected.getMode(), value.getMode());
    assertEquals(expected.getDescription(), value.getDescription());
    assertEquals(expected.getFields(), value.getFields());
  }
}
