import { FunctionComponent } from "react";
import PaginationCard from "./PaginationCard";

type PaginationControlProps = {
  numItems: number;
  itemsPerPage: number;
  currentPage: number;
  numCards: number;
}

const PaginationControl: FunctionComponent<PaginationControlProps> = ({ numItems, itemsPerPage, currentPage, numCards, setPage }) => {

  const minResult = (currentPage - 1) * itemsPerPage;
  const maxResult = minResult + (itemsPerPage - 1);
  const showControls = numItems > itemsPerPage;
  const maxPage = Math.trunc(numItems / itemsPerPage) + (numItems % itemsPerPage > 0 ? 1 : 0);
  const ellipsis = maxPage > numCards;
  const cards = [...Array(numCards).keys()];
  const renderedCards = cards.map(k => {
    return <PaginationCard regular>{k + 1}</PaginationCard>
  });

  return (
    <div className="flex items-center justify-between border-t border-gray-200 bg-white px-4 py-3 sm:px-6">
      {showControls &&
        <div className="flex flex-1 justify-between sm:hidden">
          <a href="#" className="relative inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">Previous</a>
          <a href="#" className="relative ml-3 inline-flex items-center rounded-md border border-gray-300 bg-white px-4 py-2 text-sm font-medium text-gray-700 hover:bg-gray-50">Next</a>
        </div>
      }
      <div className="hidden sm:flex sm:flex-1 sm:items-center sm:justify-between">
        <div>
          <p className="text-sm text-gray-700">
            Showing
            <span className="font-medium"> {minResult} </span>
            to
            <span className="font-medium"> {maxResult} </span>
            of
            <span className="font-medium"> {numItems} </span>
            results
            <span className="font-medium"> {maxPage} </span>
          </p>
        </div>
        {showControls &&
          <div>
            <nav aria-label="Pagination" className="isolate inline-flex -space-x-px rounded-md shadow-xs">
              {renderedCards}
              {/* <PaginationCard prev></PaginationCard>
              <PaginationCard selected>1</PaginationCard>
              <PaginationCard regular>2</PaginationCard>
              <PaginationCard regular>3</PaginationCard>
              <PaginationCard ellipsis />
              <PaginationCard regular>8</PaginationCard>
              <PaginationCard regular>9</PaginationCard>
              <PaginationCard regular>10</PaginationCard>
              <PaginationCard next></PaginationCard> */}
            </nav>
          </div>}
      </div>
    </div>
  );
}

export default PaginationControl;