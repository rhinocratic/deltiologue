import PaginationControl from "./PaginationControl";


export default function Pagination() {

  // const minResult = 1;
  // const maxResult = 10;
  // const numResults = 97;



  return (
    <PaginationControl numItems={97} itemsPerPage={10} currentPage={1} numCards={7} />
  );
}