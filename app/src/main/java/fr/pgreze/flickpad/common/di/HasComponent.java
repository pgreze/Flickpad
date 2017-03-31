package fr.pgreze.flickpad.common.di;

/** Pattern allowing to provide a component (from activity to fragment for example) */
public interface HasComponent<C> {
    C component();
}
