import type { PostcardSummary } from "../api/types/postcardSummary";
import { Link } from "react-router-dom";

interface PostcardSummaryListItemProps {
  summary: PostcardSummary;
}

export default function PostcardSummaryListItem({ summary }: PostcardSummaryListItemProps) {

  const renderedTags = (summary.tags || []).map((tag) => {
    return <div key={tag} className="border py-0.5 px-1 text-xs bg-slate-200 rounded">
      {tag}
    </div>
  });

  return (
    <div className="border p-4 rounded flex justify-between items-center">
      <div className="flex flex-col gap-2">
        <Link to={`/details/${summary.collection_index}`}>
          {summary.collection_index}: {summary.subject_description}
        </Link>
        <p className="text-sm text-gray-500">
          {summary.subject_description}
        </p>
        <div className="flex gap-1">
          {renderedTags}
        </div>
      </div>
    </div>
  );
}