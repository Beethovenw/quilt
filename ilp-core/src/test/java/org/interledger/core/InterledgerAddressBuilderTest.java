package org.interledger.core;

/*-
 * ========================LICENSE_START=================================
 * Interledger Core
 * %%
 * Copyright (C) 2017 - 2018 Hyperledger and its contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * =========================LICENSE_END==================================
 */

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * Unit tests for {@link InterledgerAddressBuilder}.
 */
public class InterledgerAddressBuilderTest {

  private static final String EXPECTED_ERROR_MESSAGE = "Address is invalid";

  private static final String TEST1_US_USD_BOB = "test1.us.usd.bob";
  private static final String TEST1_US_USD = "test1.us.usd.";
  private static final String JUST_RIGHT =
      "g.foo.0123"
          + "45678901234567890123456789012345678901234567890123456789012345678901234567890123456"
          + "78901234567890123456789012345678901234567890123456789012345678901234567890123456789"
          + "01234567890123456789012345678901234567890123456789012345678901234567890123456789012"
          + "34567890123456789012345678901234567890123456789012345678901234567890123456789012345"
          + "67890123456789012345678901234567890123456789012345678901234567890123456789012345678"
          + "90123456789012345678901234567890123456789012345678901234567890123456789012345678901"
          + "23456789012345678901234567890123456789012345678901234567890123456789012345678901234"
          + "56789012345678901234567890123456789012345678901234567890123456789012345678901234567"
          + "89012345678901234567890123456789012345678901234567890123456789012345678901234567890"
          + "12345678901234567890123456789012345678901234567890123456789012345678901234567890123"
          + "45678901234567890123456789012345678901234567890123456789012345678901234567890123456"
          + "78901234567890123456789012345678901234567890123456789012345678901234567890123456789"
          + "01234567891234567";
  private static final String TOO_LONG = JUST_RIGHT + "67890123456789012345678901234567890123456";

  @Test(expected = IllegalStateException.class)
  public void test_constructor_wit_uninitialized_build() {
    try {
      InterledgerAddress.builder().build();
    } catch (IllegalStateException e) {
      //Removed message check. This exception is raised by the Immutable generated code.
      //assertThat(e.getMessage(), is("value must not be null!"));
      throw e;
    }
  }

  @Test(expected = NullPointerException.class)
  public void test_wither_with_null_value() {
    try {
      InterledgerAddress.builder()
          .value(null)
          .build();
    } catch (NullPointerException e) {
      assertThat(e.getMessage(), is("value"));
      throw e;
    }
  }

  @Test
  public void testConstruction_DeliverableAddress() {
    final InterledgerAddress address = InterledgerAddress.builder().value(TEST1_US_USD_BOB).build();
    assertThat(address.getValue(), is(TEST1_US_USD_BOB));
    assertThat(address.isLedgerPrefix(), is(not(true)));
  }

  @Test
  public void testConstruction_LedgerPrefix() {
    final InterledgerAddress address = InterledgerAddress.builder().value(TEST1_US_USD).build();
    assertThat(address.getValue(), is(TEST1_US_USD));
    assertThat(address.isLedgerPrefix(), is(true));
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_empty_address() {
    try {
      InterledgerAddress.builder().value("").build();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_blank_address() {
    try {
      InterledgerAddress.builder().value("  ").build();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_address_with_space() {
    try {
      InterledgerAddress.builder().value(TEST1_US_USD_BOB + " space").build();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
      throw e;
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void test_address_too_long() {
    try {
      InterledgerAddress.builder().value(TOO_LONG).build();
    } catch (IllegalArgumentException e) {
      assertThat(e.getMessage(), is(EXPECTED_ERROR_MESSAGE));
      throw e;
    }
  }

  @Test
  public void test_address_just_right_length() {
    // This is 1023 characters long...
    final String value = "";
    final InterledgerAddress address = InterledgerAddress.builder().value(JUST_RIGHT).build();
    assertThat(address.getValue(), is(JUST_RIGHT));
    assertThat(address.isLedgerPrefix(), is(false));
  }

  @Test
  public void test_address_all_valid_characters() {
    final String allValues = "g.0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_~-.";
    final InterledgerAddress address = InterledgerAddress.builder().value(allValues).build();
    assertThat(address.getValue(), is(allValues));
    assertThat(address.isLedgerPrefix(), is(true));
  }
}
