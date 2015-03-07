package jihye.Vector;

public class SimilarityResult
{
	public String matchedKeyword;
	public double similarity;

	public SimilarityResult(String matchedKeyword, double similarity)
	{
		this.matchedKeyword = matchedKeyword;
		this.similarity = similarity;
	}
}
