import { searchPostcards } from "../../api/queries/searchPostcards";
import { PostcardSummary } from "../../api/types/postcardSummary";

export interface SearchLoaderResult {
  searchResults: PostcardSummary[];
  count: number;
}

export async function searchLoader({ request }: { request: Request }): Promise<SearchLoaderResult> {

  const { searchParams } = new URL(request.url);
  const term = searchParams.get("term");
  if (!term) {
    throw new Error("Search term must be provided");
  }

  const results = await searchPostcards(term);

  return {
    searchResults: results,
    count: results.length
  };
}