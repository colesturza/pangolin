/* (C)2024 */
package com.colesturza.pangolin.config;

import com.colesturza.pangolin.internal.PangolinBuilder;

/** Represents the configuration settings for a Pangolin server instance. */
public final class PangolinConfig {

  /**
   * Builder class for constructing PangolinConfig objects. Implements the PangolinBuilder interface
   * to support fluent builder pattern.
   */
  public static final class Builder implements PangolinBuilder<Builder, PangolinConfig> {

    /** Constructs a new PangolinConfig builder. */
    private Builder() {}

    /**
     * Constructs a new PangolinConfig builder initialized with values from an existing
     * PangolinConfig object.
     *
     * @param pangolinConfig the existing PangolinConfig object to initialize from
     */
    private Builder(PangolinConfig pangolinConfig) {}

    /**
     * Builds and returns a PangolinConfig object using the current builder state.
     *
     * @return a PangolinConfig object
     */
    public PangolinConfig build() {
      return new PangolinConfig(this);
    }
  }

  /**
   * Returns a new instance of the PangolinConfig builder.
   *
   * @return a PangolinConfig builder instance
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Constructs a new PangolinConfig object with the specified builder.
   *
   * @param builder the PangolinConfig builder containing configuration settings
   */
  private PangolinConfig(Builder builder) {}

  /**
   * Returns a new builder initialized with the configuration settings of this PangolinConfig
   * object.
   *
   * @return a new builder instance initialized with the configuration settings of this
   *     PangolinConfig object
   */
  public Builder toBuilder() {
    return new Builder(this);
  }
}
