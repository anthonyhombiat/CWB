/**
 * 
 */
package yamSS.simlib.linguistic;

import java.util.List;

import yamSS.main.oaei.sys.YAMSetting;
import yamSS.simlib.ext.LabelTokenizer;
import yamSS.simlib.wn.Lin;
import yamSS.system.Configs;
import yamSS.tools.Supports;
import yamSS.tools.wordnet.WordNetHelper;
import yamSS.tools.wordnet.WordNetStemmer;


/**
 * @author ngoduyhoa
 * For individual word:
 *  - matching by Soundex
 *  - matching with Abbreviation words list
 *  - Prefix,suffix  
 */
public class WordApproximate implements IStringMetric 
{
	WordNetStemmer	stemmer;
			
	public WordApproximate() 
	{
		super();
		
		try 
		{
			WordNetHelper.getInstance().initializeWN(Configs.WNDIR, Configs.WNVER);
			
			stemmer	=	WordNetHelper.getInstance().getWnstemmer();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	@Override
	public float getSimScore(String str1, String str2) 
	{		
		// TODO Auto-generated method stub
		
		if(Supports.isRandomString(str1) || Supports.isRandomString(str2))
		{
			if(str1.equalsIgnoreCase(str2))
				return 1;
			else
				return 0;
		}
		
		if(Supports.removeSpecialChars(str1).equalsIgnoreCase(Supports.removeSpecialChars(str2)))
			return 1.0f;
		/*
		if(wnStemmer.Stem(str1).equalsIgnoreCase(wnStemmer.Stem(str2)))
			return 1;
		*/		
		// using a word net metric to calculate 
		Lin	linMetric	=	new Lin();
		
		
		float	wnscore		=	linMetric.getSimScore(str1, str2);
				
		if(wnscore >= 0.9) // default: 0.9
			return wnscore;		
		
		return 0;
	}

	@Override
	public String getMetricName() {
		// TODO Auto-generated method stub
		return this.getClass().getSimpleName();
	}
	
	//////////////////////////////////////////////////////////////////////////////
	public static void main(String[] args) 
	{
		YAMSetting.init();
		/*
		String	str1	=	"Chairman";
		String	str2	=	"chair";
		*/
		
		String	str1	=	"c-0092168-5523073";
		String	str2	=	"c-0321926-9025187";
		
		
		WordApproximate	metric	=	new WordApproximate();
		
		System.out.println("score = " + metric.getSimScore(str1, str2));
	}
}
