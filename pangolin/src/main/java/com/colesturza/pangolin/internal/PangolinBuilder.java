/* (C)2024 */
package com.colesturza.pangolin.internal;

import java.util.function.Consumer;

/**
 * Represents a generic builder interface for constructing objects of type {@code T}.
 *
 * <p>This interface provides methods for building objects and applying mutations to the builder.
 * The builder implementations should be mutable, allowing modifications to the builder state during
 * the construction process. Objects constructed by the builder should be immutable to ensure thread
 * safety and prevent unintended modifications after creation.
 *
 * @param <B> the concrete type of the builder, which should extend {@code Builder<B, T>}
 * @param <T> the type of the object to be built
 */
public interface PangolinBuilder<B extends PangolinBuilder<B, T>, T> {

  /**
   * Builds and returns an object of type {@code T}.
   *
   * @return an object of type {@code T} constructed by the builder
   */
  T build();

  /**
   * Applies a mutation to the builder using the specified {@code mutator}.
   *
   * <p>The {@code mutator} is a consumer function that accepts a builder of type {@code B}. This
   * method applies the mutator function to the current builder instance. It returns the modified
   * builder instance to allow method chaining.
   *
   * @param mutator the consumer function to apply mutation to the builder
   * @return the modified builder instance after applying the mutation
   */
  @SuppressWarnings("unchecked")
  default B applyMutation(Consumer<B> mutator) {
    mutator.accept((B) this);
    return (B) this;
  }
}
