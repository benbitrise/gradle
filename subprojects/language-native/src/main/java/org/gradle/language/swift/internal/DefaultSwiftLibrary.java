/*
 * Copyright 2017 the original author or authors.
 *
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
 */

package org.gradle.language.swift.internal;

import org.gradle.api.internal.file.FileOperations;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.language.swift.SwiftLibrary;
import org.gradle.language.swift.SwiftSharedLibrary;

import javax.inject.Inject;

public class DefaultSwiftLibrary extends DefaultSwiftComponent implements SwiftLibrary {
    private final DefaultSwiftSharedLibrary debug;
    private final DefaultSwiftSharedLibrary release;

    @Inject
    public DefaultSwiftLibrary(String name, FileOperations fileOperations, ProviderFactory providerFactory) {
        super(name, fileOperations, providerFactory);
        debug = new DefaultSwiftSharedLibrary(name + "Debug", getModule(), true, getSwiftSource(), getCompileImportPath(), getLinkLibraries());
        release = new DefaultSwiftSharedLibrary(name + "Release", getModule(), false, getSwiftSource(), getCompileImportPath(), getLinkLibraries());
    }

    @Override
    public SwiftSharedLibrary getDevelopmentBinary() {
        return debug;
    }

    @Override
    public SwiftSharedLibrary getDebugSharedLibrary() {
        return debug;
    }

    @Override
    public SwiftSharedLibrary getReleaseSharedLibrary() {
        return release;
    }
}
