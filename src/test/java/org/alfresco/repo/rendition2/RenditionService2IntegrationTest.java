/*
 * #%L
 * Alfresco Repository
 * %%
 * Copyright (C) 2005 - 2018 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software.
 * If the software was purchased under a paid Alfresco license, the terms of
 * the paid license agreement will prevail.  Otherwise, the software is
 * provided under the following open source license terms:
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */
package org.alfresco.repo.rendition2;

import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.alfresco.service.cmr.repository.ChildAssociationRef;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.transaction.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for {@link RenditionService2}
 */
public class RenditionService2IntegrationTest extends AbstractRenditionIntegrationTest
{
    @Autowired
    private RenditionService2 renditionService2;
    @Autowired
    private TransformClient transformClient;
    @Autowired
    private TransactionService transactionService;

    @Before
    public void setUp()
    {
        AuthenticationUtil.setRunAsUser(AuthenticationUtil.getAdminUserName());
        assertTrue("The RenditionService2 needs to be enabled", renditionService2.isEnabled());
        assertTrue("A wrong type of transform client detected", transformClient instanceof LocalTransformClient);
    }

    // PDF transformation

    @Test
    public void testLocalRenderPdfToJpegMedium() throws Exception
    {
        checkRendition("quick.pdf", "medium", true);
    }

    @Test
    public void testLocalRenderPdfToDoclib() throws Exception
    {
        checkRendition("quick.pdf", "doclib", true);
    }

    @Test
    public void testLocalRenderPdfJpegImgpreview() throws Exception
    {
        checkRendition("quick.pdf", "imgpreview", true);
    }

    @Test
    public void testLocalRenderPdfPngAvatar() throws Exception
    {
        checkRendition("quick.pdf", "avatar", true);
    }

    @Test
    public void testLocalRenderPdfPngAvatar32() throws Exception
    {
        checkRendition("quick.pdf", "avatar32", true);
    }

    @Test
    public void testLocalRenderPdfFlashWebpreview() throws Exception
    {
        checkRendition("quick.pdf", "webpreview", false);
    }

    // DOCX transformation

    @Test
    public void testLocalRenderDocxJpegMedium() throws Exception
    {
        checkRendition("quick.docx", "medium", true);
    }

    @Test
    public void testLocalRenderDocxDoclib() throws Exception
    {
        checkRendition("quick.docx", "doclib", true);
    }

    @Test
    public void testLocalRenderDocxJpegImgpreview() throws Exception
    {
        checkRendition("quick.docx", "imgpreview", true);
    }

    @Test
    public void testLocalRenderDocxPngAvatar() throws Exception
    {
        checkRendition("quick.docx", "avatar", true);
    }

    @Test
    public void testLocalRenderDocxPngAvatar32() throws Exception
    {
        checkRendition("quick.docx", "avatar32", true);
    }

    @Test
    public void testLocalRenderDocxFlashWebpreview() throws Exception
    {
        checkRendition("quick.docx", "webpreview", false);
    }

    @Test
    public void testLocalRenderDocxPdf() throws Exception
    {
        checkRendition("quick.docx", "pdf", true);
    }

    private void checkRendition(String testFileName, String renditionDefinitionName, boolean expectedToPass) throws InterruptedException
    {
        try
        {
            NodeRef sourceNode = transactionService.getRetryingTransactionHelper().doInTransaction(() ->
            {
                NodeRef contentNode = createContentNodeFromQuickFile(testFileName);
                renditionService2.render(contentNode, renditionDefinitionName);
                return contentNode;
            });
            ChildAssociationRef childAssociationRef = null;
            for (int i = 0; i < 20; i++)
            {
                childAssociationRef = renditionService2.getRenditionByName(sourceNode, renditionDefinitionName);
                if (childAssociationRef != null)
                {
                    break;
                }
                else
                {
                    Thread.sleep(500);
                }
            }
            assertNotNull("The " + renditionDefinitionName + " rendition failed for " + testFileName, childAssociationRef);
        }
        catch(UnsupportedOperationException uoe)
        {
            if (expectedToPass)
            {
                fail("The " + renditionDefinitionName + " rendition should be supported for " + testFileName);
            }
        }
    }
}