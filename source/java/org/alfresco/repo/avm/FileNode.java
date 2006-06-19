/*
 * Copyright (C) 2006 Alfresco, Inc.
 *
 * Licensed under the Mozilla Public License version 1.1 
 * with a permitted attribution clause. You may obtain a
 * copy of the License at
 *
 *   http://www.alfresco.org/legal/license.txt
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */
package org.alfresco.repo.avm;

/**
 * Interface for the generic idea of a file.
 * @author britt
 */
interface FileNode extends AVMNode
{
    /**
     * Get the content object associated with this node, for reading.
     * @return A FileContent object.
     */
    public FileContent getContentForRead();

    /**
     * Get the content object for writing.  This will do COW
     * as needed.
     * @return A FileContent object.
     */
    public FileContent getContentForWrite();
}