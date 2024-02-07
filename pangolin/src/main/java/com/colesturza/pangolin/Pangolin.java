/* (C)2024 */
package com.colesturza.pangolin;

import com.colesturza.pangolin.config.PangolinConfig;
import java.util.function.Consumer;

public final class Pangolin {

  private final PangolinConfig pangolinConfig;

  public static Pangolin create() {
    return create(config -> {});
  }

  public static Pangolin create(Consumer<PangolinConfig.Builder> pangolinConfig) {
    return new Pangolin(PangolinConfig.builder().applyMutation(pangolinConfig).build());
  }

  private Pangolin(PangolinConfig pangolinConfig) {
    this.pangolinConfig = pangolinConfig;
  }

  public final PangolinConfig pangolinConfig() {
    return this.pangolinConfig;
  }
}
