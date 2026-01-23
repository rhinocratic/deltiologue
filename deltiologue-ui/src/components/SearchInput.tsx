import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { VscSearch } from "react-icons/vsc";

export default function SearchInput() {
  const [term, setTerm] = useState("");
  const navigate = useNavigate();

  const handleSubmit = (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    navigate(`/results?q=${term}`);
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="relative">
        <div className="absolute inset-0 pl-2 flex items-center pointer-events-none">
          <VscSearch className="h-5 w-5 text-stone-700" />
        </div>
        <input
          value={term}
          onChange={(e) => {
            console.log(term);
            setTerm(e.target.value);
          }}
          placeholder="search postcards"
          className="pl-10 py-2 w-full border-1 border-stone-300 bg-white text-stone-600 rounded-lg"
        />
      </div>
    </form>
  );
}
