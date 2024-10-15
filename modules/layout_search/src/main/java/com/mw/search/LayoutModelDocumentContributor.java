package com.mw.search;


import com.liferay.journal.model.JournalArticleDisplay;
import com.liferay.journal.service.JournalArticleLocalServiceUtil;
import com.liferay.layout.service.LayoutLocalizationLocalService;
import com.liferay.portal.kernel.language.Language;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.Layout;
import com.liferay.portal.kernel.search.Document;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portal.search.spi.model.index.contributor.ModelDocumentContributor;

import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
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
	
	private String getHTML(long groupId, String articleId, String languageId) {
		
		try {
			JournalArticleDisplay jad = JournalArticleLocalServiceUtil.getArticleDisplay(groupId, articleId, "view", languageId, null);
			
			if (jad != null) return jad.getContent();
		} catch (Exception e) {
			return null;
		}	
		
		return null;
	}

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
		
		long groupId = 20119; // HARCODED
		String articleId = "42033"; // HARCODED
		
		for (String languageId : layout.getAvailableLanguageIds()) {
			Locale locale = LocaleUtil.fromLanguageId(languageId);
				
			String html = getHTML(groupId, articleId, languageId);
				
			if (Validator.isNotNull(html)) {
				org.jsoup.nodes.Document htmlDocument = Jsoup.parse(html.toString());
					
				Element h1 = htmlDocument.selectFirst("h1");
				Element h2 = htmlDocument.selectFirst("h2");
				Element h3 = htmlDocument.selectFirst("h3");
				
				String h1Text = h1.text();
				String h2Text = h2.text();
				String h3Text = h3.text();
				
				_log.info("h1Text: " + h1Text);
				_log.info("h2Text: " + h2Text);
				_log.info("h3Text: " + h3Text);
				
				String content = htmlDocument.text();
				
				_log.info("content: " + content);
				
				document.addText(Field.getLocalizedName(locale, "h1"), h1Text);
				document.addText(Field.getLocalizedName(locale, "h2"), h2Text);
				document.addText(Field.getLocalizedName(locale, "h3"), h3Text);
					
				document.addText(Field.getLocalizedName(locale, "content"), content);	
			} else {
				document.addText(Field.getLocalizedName(locale, "h1"), "");
				document.addText(Field.getLocalizedName(locale, "h2"), "");
				document.addText(Field.getLocalizedName(locale, "h3"), "");
				
				document.addText(Field.getLocalizedName(locale, "content"), "");
			}
					
			_log.info("Added additional fields for " + layout.getPlid());		
		}
	}

	@Reference
	private Language _language;

	@Reference
	private LayoutLocalizationLocalService _layoutLocalizationLocalService;
	
	private static final Log _log = LogFactoryUtil.getLog(LayoutModelDocumentContributor.class);
}
