package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the {@code libs} extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final ActivityLibraryAccessors laccForActivityLibraryAccessors = new ActivityLibraryAccessors(owner);
    private final CoilLibraryAccessors laccForCoilLibraryAccessors = new CoilLibraryAccessors(owner);
    private final ComposeLibraryAccessors laccForComposeLibraryAccessors = new ComposeLibraryAccessors(owner);
    private final CoroutinesLibraryAccessors laccForCoroutinesLibraryAccessors = new CoroutinesLibraryAccessors(owner);
    private final FirebaseLibraryAccessors laccForFirebaseLibraryAccessors = new FirebaseLibraryAccessors(owner);
    private final KoinLibraryAccessors laccForKoinLibraryAccessors = new KoinLibraryAccessors(owner);
    private final LifecycleLibraryAccessors laccForLifecycleLibraryAccessors = new LifecycleLibraryAccessors(owner);
    private final MapsLibraryAccessors laccForMapsLibraryAccessors = new MapsLibraryAccessors(owner);
    private final PlayLibraryAccessors laccForPlayLibraryAccessors = new PlayLibraryAccessors(owner);
    private final SerializationLibraryAccessors laccForSerializationLibraryAccessors = new SerializationLibraryAccessors(owner);
    private final StripeLibraryAccessors laccForStripeLibraryAccessors = new StripeLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Dependency provider for <b>datetime</b> with <b>org.jetbrains.kotlinx:kotlinx-datetime</b> coordinates and
     * with version reference <b>datetime</b>
     * <p>
     * This dependency was declared in catalog libs.versions.toml
     */
    public Provider<MinimalExternalModuleDependency> getDatetime() {
        return create("datetime");
    }

    /**
     * Group of libraries at <b>activity</b>
     */
    public ActivityLibraryAccessors getActivity() {
        return laccForActivityLibraryAccessors;
    }

    /**
     * Group of libraries at <b>coil</b>
     */
    public CoilLibraryAccessors getCoil() {
        return laccForCoilLibraryAccessors;
    }

    /**
     * Group of libraries at <b>compose</b>
     */
    public ComposeLibraryAccessors getCompose() {
        return laccForComposeLibraryAccessors;
    }

    /**
     * Group of libraries at <b>coroutines</b>
     */
    public CoroutinesLibraryAccessors getCoroutines() {
        return laccForCoroutinesLibraryAccessors;
    }

    /**
     * Group of libraries at <b>firebase</b>
     */
    public FirebaseLibraryAccessors getFirebase() {
        return laccForFirebaseLibraryAccessors;
    }

    /**
     * Group of libraries at <b>koin</b>
     */
    public KoinLibraryAccessors getKoin() {
        return laccForKoinLibraryAccessors;
    }

    /**
     * Group of libraries at <b>lifecycle</b>
     */
    public LifecycleLibraryAccessors getLifecycle() {
        return laccForLifecycleLibraryAccessors;
    }

    /**
     * Group of libraries at <b>maps</b>
     */
    public MapsLibraryAccessors getMaps() {
        return laccForMapsLibraryAccessors;
    }

    /**
     * Group of libraries at <b>play</b>
     */
    public PlayLibraryAccessors getPlay() {
        return laccForPlayLibraryAccessors;
    }

    /**
     * Group of libraries at <b>serialization</b>
     */
    public SerializationLibraryAccessors getSerialization() {
        return laccForSerializationLibraryAccessors;
    }

    /**
     * Group of libraries at <b>stripe</b>
     */
    public StripeLibraryAccessors getStripe() {
        return laccForStripeLibraryAccessors;
    }

    /**
     * Group of versions at <b>versions</b>
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Group of bundles at <b>bundles</b>
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Group of plugins at <b>plugins</b>
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class ActivityLibraryAccessors extends SubDependencyFactory {

        public ActivityLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compose</b> with <b>androidx.activity:activity-compose</b> coordinates and
         * with version <b>1.9.3</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("activity.compose");
        }

    }

    public static class CoilLibraryAccessors extends SubDependencyFactory {

        public CoilLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compose</b> with <b>io.coil-kt:coil-compose</b> coordinates and
         * with version reference <b>coil</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("coil.compose");
        }

    }

    public static class ComposeLibraryAccessors extends SubDependencyFactory {
        private final ComposeMaterialLibraryAccessors laccForComposeMaterialLibraryAccessors = new ComposeMaterialLibraryAccessors(owner);
        private final ComposeUiLibraryAccessors laccForComposeUiLibraryAccessors = new ComposeUiLibraryAccessors(owner);

        public ComposeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>bom</b> with <b>androidx.compose:compose-bom</b> coordinates and
         * with version reference <b>compose.bom</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getBom() {
            return create("compose.bom");
        }

        /**
         * Dependency provider for <b>material3</b> with <b>androidx.compose.material3:material3</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMaterial3() {
            return create("compose.material3");
        }

        /**
         * Dependency provider for <b>navigation</b> with <b>androidx.navigation:navigation-compose</b> coordinates and
         * with version reference <b>compose.navigation</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getNavigation() {
            return create("compose.navigation");
        }

        /**
         * Group of libraries at <b>compose.material</b>
         */
        public ComposeMaterialLibraryAccessors getMaterial() {
            return laccForComposeMaterialLibraryAccessors;
        }

        /**
         * Group of libraries at <b>compose.ui</b>
         */
        public ComposeUiLibraryAccessors getUi() {
            return laccForComposeUiLibraryAccessors;
        }

    }

    public static class ComposeMaterialLibraryAccessors extends SubDependencyFactory {

        public ComposeMaterialLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>icons</b> with <b>androidx.compose.material:material-icons-extended</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getIcons() {
            return create("compose.material.icons");
        }

    }

    public static class ComposeUiLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {
        private final ComposeUiToolingLibraryAccessors laccForComposeUiToolingLibraryAccessors = new ComposeUiToolingLibraryAccessors(owner);

        public ComposeUiLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>ui</b> with <b>androidx.compose.ui:ui</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("compose.ui");
        }

        /**
         * Dependency provider for <b>graphics</b> with <b>androidx.compose.ui:ui-graphics</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getGraphics() {
            return create("compose.ui.graphics");
        }

        /**
         * Group of libraries at <b>compose.ui.tooling</b>
         */
        public ComposeUiToolingLibraryAccessors getTooling() {
            return laccForComposeUiToolingLibraryAccessors;
        }

    }

    public static class ComposeUiToolingLibraryAccessors extends SubDependencyFactory implements DependencyNotationSupplier {

        public ComposeUiToolingLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>tooling</b> with <b>androidx.compose.ui:ui-tooling</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> asProvider() {
            return create("compose.ui.tooling");
        }

        /**
         * Dependency provider for <b>preview</b> with <b>androidx.compose.ui:ui-tooling-preview</b> coordinates and
         * with <b>no version specified</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getPreview() {
            return create("compose.ui.tooling.preview");
        }

    }

    public static class CoroutinesLibraryAccessors extends SubDependencyFactory {

        public CoroutinesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>android</b> with <b>org.jetbrains.kotlinx:kotlinx-coroutines-android</b> coordinates and
         * with version reference <b>coroutines</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAndroid() {
            return create("coroutines.android");
        }

        /**
         * Dependency provider for <b>core</b> with <b>org.jetbrains.kotlinx:kotlinx-coroutines-core</b> coordinates and
         * with version reference <b>coroutines</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("coroutines.core");
        }

    }

    public static class FirebaseLibraryAccessors extends SubDependencyFactory {

        public FirebaseLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>auth</b> with <b>dev.gitlive:firebase-auth</b> coordinates and
         * with version reference <b>firebase.kotlin</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAuth() {
            return create("firebase.auth");
        }

        /**
         * Dependency provider for <b>firestore</b> with <b>dev.gitlive:firebase-firestore</b> coordinates and
         * with version reference <b>firebase.kotlin</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getFirestore() {
            return create("firebase.firestore");
        }

    }

    public static class KoinLibraryAccessors extends SubDependencyFactory {

        public KoinLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>android</b> with <b>io.insert-koin:koin-android</b> coordinates and
         * with version reference <b>koin</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAndroid() {
            return create("koin.android");
        }

        /**
         * Dependency provider for <b>compose</b> with <b>io.insert-koin:koin-androidx-compose</b> coordinates and
         * with version reference <b>koin.compose</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("koin.compose");
        }

        /**
         * Dependency provider for <b>core</b> with <b>io.insert-koin:koin-core</b> coordinates and
         * with version reference <b>koin</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCore() {
            return create("koin.core");
        }

    }

    public static class LifecycleLibraryAccessors extends SubDependencyFactory {

        public LifecycleLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>runtime</b> with <b>androidx.lifecycle:lifecycle-runtime-compose</b> coordinates and
         * with version reference <b>lifecycle</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getRuntime() {
            return create("lifecycle.runtime");
        }

        /**
         * Dependency provider for <b>viewmodel</b> with <b>androidx.lifecycle:lifecycle-viewmodel-compose</b> coordinates and
         * with version reference <b>lifecycle</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getViewmodel() {
            return create("lifecycle.viewmodel");
        }

    }

    public static class MapsLibraryAccessors extends SubDependencyFactory {

        public MapsLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>compose</b> with <b>com.google.maps.android:maps-compose</b> coordinates and
         * with version reference <b>maps.compose</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getCompose() {
            return create("maps.compose");
        }

    }

    public static class PlayLibraryAccessors extends SubDependencyFactory {
        private final PlayServicesLibraryAccessors laccForPlayServicesLibraryAccessors = new PlayServicesLibraryAccessors(owner);

        public PlayLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Group of libraries at <b>play.services</b>
         */
        public PlayServicesLibraryAccessors getServices() {
            return laccForPlayServicesLibraryAccessors;
        }

    }

    public static class PlayServicesLibraryAccessors extends SubDependencyFactory {

        public PlayServicesLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>location</b> with <b>com.google.android.gms:play-services-location</b> coordinates and
         * with version reference <b>play.services.location</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getLocation() {
            return create("play.services.location");
        }

        /**
         * Dependency provider for <b>maps</b> with <b>com.google.android.gms:play-services-maps</b> coordinates and
         * with version reference <b>play.services.maps</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getMaps() {
            return create("play.services.maps");
        }

    }

    public static class SerializationLibraryAccessors extends SubDependencyFactory {

        public SerializationLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>json</b> with <b>org.jetbrains.kotlinx:kotlinx-serialization-json</b> coordinates and
         * with version reference <b>serialization</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getJson() {
            return create("serialization.json");
        }

    }

    public static class StripeLibraryAccessors extends SubDependencyFactory {

        public StripeLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Dependency provider for <b>android</b> with <b>com.stripe:stripe-android</b> coordinates and
         * with version reference <b>stripe</b>
         * <p>
         * This dependency was declared in catalog libs.versions.toml
         */
        public Provider<MinimalExternalModuleDependency> getAndroid() {
            return create("stripe.android");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final ComposeVersionAccessors vaccForComposeVersionAccessors = new ComposeVersionAccessors(providers, config);
        private final FirebaseVersionAccessors vaccForFirebaseVersionAccessors = new FirebaseVersionAccessors(providers, config);
        private final GoogleVersionAccessors vaccForGoogleVersionAccessors = new GoogleVersionAccessors(providers, config);
        private final KoinVersionAccessors vaccForKoinVersionAccessors = new KoinVersionAccessors(providers, config);
        private final MapsVersionAccessors vaccForMapsVersionAccessors = new MapsVersionAccessors(providers, config);
        private final PlayVersionAccessors vaccForPlayVersionAccessors = new PlayVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>agp</b> with value <b>8.7.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getAgp() { return getVersion("agp"); }

        /**
         * Version alias <b>coil</b> with value <b>2.7.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCoil() { return getVersion("coil"); }

        /**
         * Version alias <b>coroutines</b> with value <b>1.9.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCoroutines() { return getVersion("coroutines"); }

        /**
         * Version alias <b>datetime</b> with value <b>0.6.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getDatetime() { return getVersion("datetime"); }

        /**
         * Version alias <b>kotlin</b> with value <b>2.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getKotlin() { return getVersion("kotlin"); }

        /**
         * Version alias <b>lifecycle</b> with value <b>2.8.7</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLifecycle() { return getVersion("lifecycle"); }

        /**
         * Version alias <b>serialization</b> with value <b>1.7.3</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getSerialization() { return getVersion("serialization"); }

        /**
         * Version alias <b>stripe</b> with value <b>20.55.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getStripe() { return getVersion("stripe"); }

        /**
         * Group of versions at <b>versions.compose</b>
         */
        public ComposeVersionAccessors getCompose() {
            return vaccForComposeVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.firebase</b>
         */
        public FirebaseVersionAccessors getFirebase() {
            return vaccForFirebaseVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.google</b>
         */
        public GoogleVersionAccessors getGoogle() {
            return vaccForGoogleVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.koin</b>
         */
        public KoinVersionAccessors getKoin() {
            return vaccForKoinVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.maps</b>
         */
        public MapsVersionAccessors getMaps() {
            return vaccForMapsVersionAccessors;
        }

        /**
         * Group of versions at <b>versions.play</b>
         */
        public PlayVersionAccessors getPlay() {
            return vaccForPlayVersionAccessors;
        }

    }

    public static class ComposeVersionAccessors extends VersionFactory  {

        public ComposeVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>compose.bom</b> with value <b>2024.12.01</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getBom() { return getVersion("compose.bom"); }

        /**
         * Version alias <b>compose.navigation</b> with value <b>2.8.5</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getNavigation() { return getVersion("compose.navigation"); }

    }

    public static class FirebaseVersionAccessors extends VersionFactory  {

        public FirebaseVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>firebase.kotlin</b> with value <b>2.1.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getKotlin() { return getVersion("firebase.kotlin"); }

    }

    public static class GoogleVersionAccessors extends VersionFactory  {

        public GoogleVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>google.services</b> with value <b>4.4.2</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getServices() { return getVersion("google.services"); }

    }

    public static class KoinVersionAccessors extends VersionFactory  implements VersionNotationSupplier {

        public KoinVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>koin</b> with value <b>4.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> asProvider() { return getVersion("koin"); }

        /**
         * Version alias <b>koin.compose</b> with value <b>4.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCompose() { return getVersion("koin.compose"); }

    }

    public static class MapsVersionAccessors extends VersionFactory  {

        public MapsVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>maps.compose</b> with value <b>6.2.1</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getCompose() { return getVersion("maps.compose"); }

    }

    public static class PlayVersionAccessors extends VersionFactory  {

        private final PlayServicesVersionAccessors vaccForPlayServicesVersionAccessors = new PlayServicesVersionAccessors(providers, config);
        public PlayVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Group of versions at <b>versions.play.services</b>
         */
        public PlayServicesVersionAccessors getServices() {
            return vaccForPlayServicesVersionAccessors;
        }

    }

    public static class PlayServicesVersionAccessors extends VersionFactory  {

        public PlayServicesVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Version alias <b>play.services.location</b> with value <b>21.3.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getLocation() { return getVersion("play.services.location"); }

        /**
         * Version alias <b>play.services.maps</b> with value <b>19.0.0</b>
         * <p>
         * If the version is a rich version and cannot be represented as a
         * single version string, an empty string is returned.
         * <p>
         * This version was declared in catalog libs.versions.toml
         */
        public Provider<String> getMaps() { return getVersion("play.services.maps"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Plugin provider for <b>androidApplication</b> with plugin id <b>com.android.application</b> and
         * with version reference <b>agp</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getAndroidApplication() { return createPlugin("androidApplication"); }

        /**
         * Plugin provider for <b>androidLibrary</b> with plugin id <b>com.android.library</b> and
         * with version reference <b>agp</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getAndroidLibrary() { return createPlugin("androidLibrary"); }

        /**
         * Plugin provider for <b>composeCompiler</b> with plugin id <b>org.jetbrains.kotlin.plugin.compose</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getComposeCompiler() { return createPlugin("composeCompiler"); }

        /**
         * Plugin provider for <b>googleServices</b> with plugin id <b>com.google.gms.google-services</b> and
         * with version reference <b>google.services</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getGoogleServices() { return createPlugin("googleServices"); }

        /**
         * Plugin provider for <b>kotlinAndroid</b> with plugin id <b>org.jetbrains.kotlin.android</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getKotlinAndroid() { return createPlugin("kotlinAndroid"); }

        /**
         * Plugin provider for <b>kotlinMultiplatform</b> with plugin id <b>org.jetbrains.kotlin.multiplatform</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getKotlinMultiplatform() { return createPlugin("kotlinMultiplatform"); }

        /**
         * Plugin provider for <b>kotlinSerialization</b> with plugin id <b>org.jetbrains.kotlin.plugin.serialization</b> and
         * with version reference <b>kotlin</b>
         * <p>
         * This plugin was declared in catalog libs.versions.toml
         */
        public Provider<PluginDependency> getKotlinSerialization() { return createPlugin("kotlinSerialization"); }

    }

}
