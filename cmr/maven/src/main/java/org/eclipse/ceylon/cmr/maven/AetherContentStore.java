/********************************************************************************
 * Copyright (c) 2011-2017 Red Hat Inc. and/or its affiliates and others
 *
 * This program and the accompanying materials are made available under the 
 * terms of the Apache License, Version 2.0 which is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0 
 ********************************************************************************/
package org.eclipse.ceylon.cmr.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import org.eclipse.ceylon.cmr.api.ArtifactContext;
import org.eclipse.ceylon.cmr.impl.AbstractContentStore;
import org.eclipse.ceylon.cmr.impl.DefaultNode;
import org.eclipse.ceylon.cmr.impl.RootNode;
import org.eclipse.ceylon.cmr.spi.ContentHandle;
import org.eclipse.ceylon.cmr.spi.ContentOptions;
import org.eclipse.ceylon.cmr.spi.Node;
import org.eclipse.ceylon.cmr.spi.OpenNode;
import org.eclipse.ceylon.cmr.spi.SizedInputStream;
import org.eclipse.ceylon.common.log.Logger;

/**
 * Sonatype Aether content store.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class AetherContentStore extends AbstractContentStore {

    private final AetherUtils utils;
    private final String settingsXml;

    public AetherContentStore(Logger log, String settingsXml, String rootFolderOverride, boolean offline, int timeout, String currentDirectory) {
        super(log, offline, timeout);
        utils = new AetherUtils(log, settingsXml, rootFolderOverride, offline, timeout, currentDirectory);
        this.settingsXml = settingsXml;
    }

    AetherUtils getUtils() {
        return utils;
    }

    public Iterable<File> getBaseDirectories() {
        return Arrays.asList(utils.getLocalRepositoryBaseDir());
    }

    public OpenNode createRoot() {
        return new RootNode(this, this);
    }

    public OpenNode find(Node parent, String child) {
        DefaultNode node = null;
        if (hasContent(child) == false) {
            node = new DefaultNode(child);
            node.setContentMarker();
        } else {
            final File dependency = utils.findDependency(parent);
            if (dependency != null) {
                node = new DefaultNode(child);
                node.setHandle(new FileContentHandle(dependency));
            }
        }
        return node;
    }

    public ContentHandle peekContent(Node node) {
        final File dependency = utils.findDependency(node);
        return (dependency != null) ? new FileContentHandle(dependency) : null;
    }

    public ContentHandle getContent(Node node) throws IOException {
        return new AetherContentHandle(node);
    }

    public ContentHandle putContent(Node node, InputStream stream, ContentOptions options) throws IOException {
        return null;  // cannot put content
    }

    public OpenNode create(Node parent, String child) {
        return null; // cannot create
    }

    public Iterable<? extends OpenNode> find(Node parent) {
        return Collections.emptyList(); // cannot find all children
    }

    private static class FileContentHandle implements ContentHandle {
        private final File file;

        private FileContentHandle(File file) {
            this.file = file;
        }

        public boolean hasBinaries() {
            return true;
        }

        public InputStream getBinariesAsStream() throws IOException {
            return new FileInputStream(file);
        }

        public SizedInputStream getBinariesAsSizedStream() throws IOException {
            return new SizedInputStream(getBinariesAsStream(), file.length());
        }

        public File getContentAsFile() throws IOException {
            return file;
        }

        public long getLastModified() throws IOException {
            return file.lastModified();
        }

        public long getSize() throws IOException {
            return file.length();
        }

        public void clean() {
        }
    }

    private class AetherContentHandle implements ContentHandle {
        private Node node;

        private AetherContentHandle(Node node) {
            this.node = node;
        }

        public boolean hasBinaries() {
            return true;
        }

        public InputStream getBinariesAsStream() throws IOException {
            return new FileInputStream(getContentAsFile());
        }

        public SizedInputStream getBinariesAsSizedStream() throws IOException {
            File file = getContentAsFile();
            return new SizedInputStream(new FileInputStream(file), file.length());
        }

        public File getContentAsFile() throws IOException {
            final ArtifactContext ac = ArtifactContext.fromNode(node);
            if (ac == null)
                throw new IOException("Missing artifact context info!");

            return utils.findDependency(node);
        }

        public long getLastModified() throws IOException {
            return getContentAsFile().lastModified();
        }

        public long getSize() throws IOException {
            return getContentAsFile().length();
        }

        public void clean() {
        }
    }

    public String getDisplayString() {
        String name = "Aether";
        if (settingsXml!=null) {
            name += ":" + settingsXml;
        }
        if (offline) {
            name += " (offline)";
        }
        return name;
    }
    
    @Override
    public String toString() {
        return "AetherContentStore: " + getDisplayString();
    }



    @Override
    public boolean isHerd() {
        return false;
    }

    @Override
    public boolean canHandleFolders() {
        return false;
    }
}
