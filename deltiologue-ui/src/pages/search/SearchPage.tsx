import { useLoaderData } from "react-router-dom";
import { SearchLoaderResult } from "./searchLoader";
import PostcardSummaryListItem from "../../components/PostcardSummaryListItem";

export default function SearchPage() {
  const { searchResults } = useLoaderData() as SearchLoaderResult;

  const renderedResults = searchResults.map((summary) => {
    return <PostcardSummaryListItem summary={summary} key={summary.collection_index} />
  });

  return <div>
    <h1 className="text-2xl font-bold my-6">Search Page</h1>
    <div className="space-y-4 mt-4">
      {renderedResults}
    </div>
  </div>;
}
