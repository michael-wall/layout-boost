package com.mw.search;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.search.BooleanQuery;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.SearchContext;
import com.liferay.portal.search.query.QueryHelper;
import com.liferay.portal.search.spi.model.query.contributor.KeywordQueryContributor;
import com.liferay.portal.search.spi.model.query.contributor.helper.KeywordQueryContributorHelper;

import java.util.Locale;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(
		property = "indexer.class.name=com.liferay.portal.kernel.model.Layout",
		service = KeywordQueryContributor.class
	)
public class LayoutKeywordQueryContributor implements KeywordQueryContributor {

		@Override
		public void contribute(
			String keywords, BooleanQuery booleanQuery,
			KeywordQueryContributorHelper keywordQueryContributorHelper) {
			
			SearchContext searchContext =
				keywordQueryContributorHelper.getSearchContext();
			
			Locale locale = searchContext.getLocale();
			
			_log.info(">>>>" + Field.getLocalizedName(locale, "h1"));

			queryHelper.addSearchTerm(booleanQuery, searchContext, Field.getLocalizedName(locale, "h1"), true);

			queryHelper.addSearchTerm(booleanQuery, searchContext, Field.getLocalizedName(locale, "h2"), true);
			
			queryHelper.addSearchTerm(booleanQuery, searchContext, Field.getLocalizedName(locale, "h3"), true);
			
			queryHelper.addSearchTerm(booleanQuery, searchContext, Field.getLocalizedName(locale, "h4"), true);
			
			queryHelper.addSearchTerm(booleanQuery, searchContext, Field.getLocalizedName(locale, "h5"), true);
		}

		@Reference
		protected QueryHelper queryHelper;
		
		private static final Log _log = LogFactoryUtil.getLog(LayoutKeywordQueryContributor.class);		
	}