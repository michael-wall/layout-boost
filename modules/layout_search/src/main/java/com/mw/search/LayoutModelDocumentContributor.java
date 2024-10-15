package com.mw.search;


import com.liferay.layout.service.LayoutLocalizationLocalService;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
	immediate = true,
	property = { 
			"indexer.class.name=com.liferay.portal.kernel.model.Layout",
	},
	service = ModelDocumentContributor.class
)
public class LayoutModelDocumentContributor
	implements ModelDocumentContributor<Layout> {

	public static final String CLASS_NAME = Layout.class.getName();

	@Override
	public void contribute(Document document, Layout layout) {
		
		if (layout.isSystem() ||
			(layout.getStatus() != WorkflowConstants.STATUS_APPROVED)) {

			return;
		}
		
		boolean isPrivateLayout = layout.isPrivateLayout();
		
		if (isPrivateLayout) {
			_log.info("Indexing additional fields for " + layout.getPlid());			
		} else {
			return;
		}
		
		if (layout.getPlid() == 145) { //Football
			for (String languageId : layout.getAvailableLanguageIds()) {
				Locale locale = LocaleUtil.fromLanguageId(languageId);

				document.addText(Field.getLocalizedName(locale, "h1"), "header");				
				document.addText(Field.getLocalizedName(locale, "h2"), "cheese");
				document.addText(Field.getLocalizedName(locale, "h3"), "soup");
				document.addText(Field.getLocalizedName(locale, "h4"), "yoghurt");
				document.addText(Field.getLocalizedName(locale, "h5"), "bread");
					
				_log.info("Added additional fields for " + layout.getPlid());
			}			
		} else if (layout.getPlid() == 146) { //Search
			for (String languageId : layout.getAvailableLanguageIds()) {
				Locale locale = LocaleUtil.fromLanguageId(languageId);

				document.addText(Field.getLocalizedName(locale, "h1"), "bread");				
				document.addText(Field.getLocalizedName(locale, "h2"), "yoghurt");
				document.addText(Field.getLocalizedName(locale, "h3"), "soup");
				document.addText(Field.getLocalizedName(locale, "h4"), "cheese");
				document.addText(Field.getLocalizedName(locale, "h5"), "header");
					
				_log.info("Added additional fields for " + layout.getPlid());
			}			
		}
	}

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalizationLocalService _layoutLocalizationLocalService;
	
	private static final Log _log = LogFactoryUtil.getLog(LayoutModelDocumentContributor.class);
}
