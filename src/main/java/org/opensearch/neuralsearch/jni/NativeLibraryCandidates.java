/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package org.opensearch.neuralsearch.jni;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The set of shared-library names to try when loading the native sparse engine.
 * <p>
 * Kept separate from {@link NativeLibrary} on purpose: NativeLibrary's static
 * initializer loads the library, so touching any of its static state pulls the
 * shared object in. This class holds the naming policy with no side effects, so
 * it can be asserted against jni/cmake/init-nsparse.cmake in a plain unit test.
 */
final class NativeLibraryCandidates {

    static final String LIBRARY_NAME = "opensearch_neuralsearch_nsparse";

    /**
     * jni/cmake/init-nsparse.cmake picks one SIMD variant at build time and appends it to
     * the library name, so the file on disk is rarely the bare LIBRARY_NAME. Only one
     * variant is ever built, so the suffixes are tried most-specific first with the
     * unsuffixed (generic) build last.
     * <p>
     * Must stay in sync with the {@code string(PREPEND LIB_EXT ...)} calls in
     * init-nsparse.cmake; NativeLibraryContractTests pins the pairing.
     */
    static final List<String> SIMD_SUFFIXES = List.of("_avx512", "_avx2", "_sve", "");

    /** Library names to try, in order. */
    static List<String> candidates() {
        return SIMD_SUFFIXES.stream().map(suffix -> LIBRARY_NAME + suffix).collect(Collectors.toUnmodifiableList());
    }

    private NativeLibraryCandidates() {}
}
