/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package concerttours.setup;

import concerttours.constants.ConcerttoursConstants;
import concerttours.service.ConcerttoursService;
import de.hybris.platform.core.initialization.SystemSetup;

import java.io.InputStream;

import static concerttours.constants.ConcerttoursConstants.PLATFORM_LOGO_CODE;


@SystemSetup(extension = ConcerttoursConstants.EXTENSIONNAME)
public class ConcerttoursSystemSetup
{
	private final ConcerttoursService concerttoursService;

	public ConcerttoursSystemSetup(final ConcerttoursService concerttoursService)
	{
		this.concerttoursService = concerttoursService;
	}

	@SystemSetup(process = SystemSetup.Process.INIT, type = SystemSetup.Type.ESSENTIAL)
	public void createEssentialData()
	{
		concerttoursService.createLogo(PLATFORM_LOGO_CODE);
	}

	private InputStream getImageStream()
	{
		return ConcerttoursSystemSetup.class.getResourceAsStream("/concerttours/sap-hybris-platform.png");
	}
}
