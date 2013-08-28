package sm.nlp.oogle.test;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sm.nlp.oogle.datastore.SpellingCorrectionCandidate;
import sm.nlp.oogle.word_correction.SpellingCorrector;

public class TestSpellingCorrection {
	public static void main(String[] args) {
		SpellingCorrector corrector = SpellingCorrector.corrector;
		DecimalFormat format = new DecimalFormat("#.#######");
		ArrayList<String> wordList = new ArrayList<String>();
		
		wordList.add("belive");
		wordList.add("bouyant");
		wordList.add("comitte");
		wordList.add("distarct");
		wordList.add("extacy");
		
		wordList.add("failr");
		wordList.add("hellpp");
		wordList.add("gracefull");
		wordList.add("liason");
		wordList.add("ocassion");
		
		wordList.add("possable");
		wordList.add("thruout");
		wordList.add("volly");
		wordList.add("tatoos");
		wordList.add("respe");
		
		
		for(String word : wordList){
			List<SpellingCorrectionCandidate> list = corrector.correct(word);
			
			StringBuilder sb = new StringBuilder();
			sb.append(word+"\t");
			for(SpellingCorrectionCandidate candidate : list){
				sb.append(candidate.getWord()+"\t"+format.format(candidate.getNormalized_probability())+"\t");
			}
			
			System.out.println(sb.toString().trim());
		}
	}
}
/**
Output:~~
belive	belive	1
bouyant	bowyang	0.8516689	buoyant	0.1161361	rousant	0.0151793	bouffant	0.0099754	courant	0.0070403
comitte	fomite	0.5450085	committee	0.249273	comate	0.0654903	comity	0.0372955	somite	0.0296553	cocotte	0.0277251	comice	0.0269195	comte	0.0095439	committed	0.0075977	comique	0.001187	comitia	0.000164	cogitate	0.0001059	compote	0.0000171	commote	0.0000054	compete	0.0000039	comities	0.0000033	compute	0.0000021	comital	0.0000018	commute	0.0000007
distarct	distract	0.5875773	distinct	0.2862951	distant	0.0550505	distort	0.0507479	district	0.0089673	distance	0.0069685	dispart	0.0033791	distracts	0.0009864	distrait	0.0000279
extacy	extasy	0.9824513	eustacy	0.0137613	exact	0.0030085	eutaxy	0.0003366	extract	0.0003077	extant	0.0001346
failr	fair	0.408887	fail	0.2109276	vair	0.0850776	vail	0.0720736	vails	0.07025	fails	0.0504017	pair	0.0270088	fall	0.006356	hair	0.0062007	far	0.0061597	faix	0.0045852	jail	0.0040041	jails	0.0039028	maill	0.0031275	fauld	0.0027847	fain	0.0025999	faint	0.0024363	pail	0.0022881	pails	0.0022302	foil	0.0019481
hellpp	help	0.9961687	helps	0.0017551	hello	0.0009592	hells	0.0008879	hell	0.0001696	hellos	0.0000468	helled	0.0000076	heller	0.0000052
gracefull	graceful	0.9953895	gracefully	0.0032079	gronefull	0.0007181	grateful	0.0006757	gracefuller	0.0000059	gratefully	0.0000029
liason	liaison	0.5292953	reason	0.271441	lissom	0.0745466	season	0.0439094	diaxon	0.0383324	mason	0.0123198	bason	0.0079538	sialon	0.0044078	geason	0.0029777	weason	0.0024023	leasow	0.0022142	vison	0.0021314	lesson	0.0020276	limacon	0.0015487	lion	0.0014855	peason	0.001336	bison	0.0010127	bisson	0.0002916	liaisons	0.0002605	wilson	0.0000823
ocassion	occasion	0.8307082	omission	0.1691071	scission	0.0001768	scansion	0.0000078
possable	possible	0.8716568	passable	0.1232143	possibly	0.0015614	poseable	0.001344	passible	0.0010467	passably	0.0006728	parsable	0.0001182	kissable	0.0001	losable	0.0000901	potable	0.0000604	crossable	0.000049	possibler	0.0000425	possibles	0.0000205	missable	0.0000186	portable	0.0000046	pourable	0.0000003
thruout	thruput	0.9639877	tryout	0.0316597	thrust	0.0018093	thruputs	0.0015455	trout	0.0004499	throat	0.0003157	thereout	0.0001948	turnout	0.0000374
volly	colly	0.303589	folly	0.2044458	jolly	0.1568839	polly	0.1277628	golly	0.0679234	wolly	0.0516022	molly	0.0237335	holly	0.0223413	dolly	0.0129108	coly	0.0039463	lolly	0.0030481	pally	0.0028596	coolly	0.0022015	poly	0.0017539	jelly	0.0017239	felly	0.0017239	fonly	0.0016581	colls	0.0011179	wally	0.0010723	coldly	0.0009418
tatoos	tattoos	0.8256506	tatous	0.0753777	ratoos	0.0346956	taboos	0.0336564	datos	0.0091913	gazoos	0.0073628	tates	0.0029325	tattoo	0.0025767	potoos	0.0020598	taties	0.0018802	jatos	0.0009541	tacos	0.0007032	dattos	0.0004894	matzos	0.0002296	tatus	0.0002056	patois	0.0001865	patios	0.0001865	taros	0.0001529	razoos	0.000152	ratos	0.0001512
respe	rose	0.45454	recce	0.065223	rope	0.0496465	resee	0.0446979	rise	0.0436743	raspy	0.0348393	reave	0.0315012	jaspe	0.0298423	rase	0.0261136	repo	0.0229243	rape	0.0167563	rest	0.0147611	respot	0.0102945	rescue	0.0101606	rasp	0.0099667	ruse	0.0093422	rasper	0.0085245	rebbe	0.0069391	resize	0.0067235	rede	0.0064408
*/