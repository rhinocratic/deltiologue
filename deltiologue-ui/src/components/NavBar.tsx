import { Link } from "react-router-dom";
import LoginBar from "./LoginBar";
import SearchInput from "./SearchInput";

export default function NavBar() {
  return (
    <nav className="flex items-center justify-between flex-wrap bg-stone-200 p-6">
      <div className="flex items-center flex-shrink-0 text-stone-600 mr-6">
        {/* <svg className="fill-current h-8 w-8 mr-2" width="54" height="54" viewBox="0 0 54 54" xmlns="http://www.w3.org/2000/svg"><path d="M13.5 22.1c1.8-7.2 6.3-10.8 13.5-10.8 10.8 0 12.15 8.1 17.55 9.45 3.6.9 6.75-.45 9.45-4.05-1.8 7.2-6.3 10.8-13.5 10.8-10.8 0-12.15-8.1-17.55-9.45-3.6-.9-6.75.45-9.45 4.05zM0 38.3c1.8-7.2 6.3-10.8 13.5-10.8 10.8 0 12.15 8.1 17.55 9.45 3.6.9 6.75-.45 9.45-4.05-1.8 7.2-6.3 10.8-13.5 10.8-10.8 0-12.15-8.1-17.55-9.45-3.6-.9-6.75.45-9.45 4.05z"/></svg> */}
        <span className="font-semibold text-xl tracking-tight">Postcards of Morecambe</span>
      </div>
      <div className="block lg:hidden">
        <button className="flex items-center px-3 py-2 border rounded text-stone-600 border-teal-400 hover:text-black hover:border-white">
          <svg className="fill-current h-3 w-3" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg"><title>Menu</title><path d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z" /></svg>
        </button>
      </div>
      <div className="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
        <div className="text-sm lg:flex-grow">
          <Link state={{ idx: 0 }} to="/" className="block mt-4 lg:inline-block lg:mt-0 text-stone-600  hover:text-red-900 mr-4">
            Home
          </Link>
          <Link state={{ idx: 1 }} to="/catalogue" className="block mt-4 lg:inline-block lg:mt-0 text-stone-600  hover:text-red-900 mr-4">
            Catalogue
          </Link>
          <Link state={{ idx: 2 }} to="/notes" className="block mt-4 lg:inline-block lg:mt-0 text-stone-600 hover:text-red-900 mr-4">
            Notes
          </Link>
          <Link state={{ idx: 3 }} to="/about" className="block mt-4 lg:inline-block lg:mt-0 text-stone-600  hover:text-red-900">
            About
          </Link>
        </div>

        {/* <div>
          <a href="#" className="inline-block text-sm px-4 py-2 leading-none border rounded text-black border-white hover:border-transparent hover:text-teal-500 hover:bg-white mt-4 lg:mt-0">Download</a>
        </div> */}
        <div>
          <LoginBar />
          <SearchInput />
        </div>
      </div>
    </nav>
  );
}