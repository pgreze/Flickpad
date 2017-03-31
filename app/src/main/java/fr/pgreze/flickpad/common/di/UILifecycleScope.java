package fr.pgreze.flickpad.common.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Scope for dependencies with a limited lifecycle related to UI (activity, service).
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface UILifecycleScope {
}
