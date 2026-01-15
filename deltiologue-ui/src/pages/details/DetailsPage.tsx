import { useLoaderData } from "react-router-dom";
import { DetailsLoaderResult } from "./detailsLoader";
import { toShortDate } from "../../util/dates";


export default function DetailsPage() {
  const { details } = useLoaderData() as DetailsLoaderResult;

  return <div>
    Details Page
    <div>
      {details.notes}
    </div>
    <div>
      Publication date: {toShortDate(details.publication_date)}
    </div>
    <div>
      Publication day: {details.publication_day}
    </div>
  </div>;
}
