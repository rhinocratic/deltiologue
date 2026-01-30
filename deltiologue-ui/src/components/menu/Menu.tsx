import { useContext } from "react";
import { Link, RouterProvider, useInRouterContext } from "react-router-dom";

interface MenuItem {
  url: string;
  title: string;
}

export function Menu({ links }: { links: MenuItem[] }) {

  const renderedLinks = links.map(({ url, title }) => {
    return (
      <Link to={url} className="block mt-4 lg:inline-block lg:mt-0 text-stone-600  hover:text-red-900 mr-4">
        {title}
      </Link>
    );
  });

  return (
    <div className="w-full block flex-grow lg:flex lg:items-center lg:w-auto">
      <div className="text-sm lg:flex-grow">
        {renderedLinks}
      </div>
    </div>
  );
}