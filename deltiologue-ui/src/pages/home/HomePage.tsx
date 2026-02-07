import { useAuth0 } from "@auth0/auth0-react";
import ApiCall from "../../components/ApiCall";
import { useLocation } from "react-router-dom";
import FormExample from "../../components/forms/FormExample";
import PaginationExample from "../../components/pagination/PaginationExample";
import TagCategoryForm from "../../components/forms/TagCategoryForm";
import TagForm from "../../components/forms/TagForm";
import PublisherForm from "../../components/forms/PublisherForm";
import SeriesForm from "../../components/forms/SeriesForm";
import CardForm from "../../components/forms/CardForm";

export default function HomePage() {

  const { isAuthenticated, user, error } = useAuth0<{
    name: string;
  }>();
  const { pathname } = useLocation();

  console.log("Authenticated: " + isAuthenticated);
  console.log(error);

  const card = {
    "tags": [
      {
        "tag_id": 67,
        "tag_text": "Marine Road West",
        "tag_name": "marine_road_west",
        "category_id": 3,
        "category_text": "Location",
        "category_name": "location",
        "colour": "eeffee"
      },
      {
        "tag_id": 73,
        "tag_text": "Promenade",
        "tag_name": "promenade",
        "category_id": 4,
        "category_text": "Miscellaneous",
        "category_name": "misc",
        "colour": "eeeeee"
      },
      {
        "tag_id": 80,
        "tag_text": "Alhambra Theatre",
        "tag_name": "alhambra_theatre",
        "category_id": 1,
        "category_text": "Notable Buildings",
        "category_name": "notable_buildings",
        "colour": "eeeeff"
      },
      {
        "tag_id": 94,
        "tag_text": "West end",
        "tag_name": "west_end",
        "category_id": 8,
        "category_text": "Secondary Area",
        "category_name": "secondary_area",
        "colour": "ddffee"
      },
      {
        "tag_id": 121,
        "tag_text": "gardens",
        "tag_name": "gardens",
        "category_id": 4,
        "category_text": "Miscellaneous",
        "category_name": "misc",
        "colour": "eeeeee"
      },
      {
        "tag_id": 146,
        "tag_text": "Alexandra Road",
        "tag_name": "alexandra_road",
        "category_id": 3,
        "category_text": "Location",
        "category_name": "location",
        "colour": "eeffee"
      },
      {
        "tag_id": 162,
        "tag_text": "Regent Road",
        "tag_name": "regent_road",
        "category_id": 3,
        "category_text": "Location",
        "category_name": "location",
        "colour": "eeffee"
      },
      {
        "tag_id": 164,
        "tag_text": "West End Road",
        "tag_name": "west_end_road",
        "category_id": 3,
        "category_text": "Location",
        "category_name": "location",
        "colour": "eeffee"
      },
      {
        "tag_id": 203,
        "tag_text": "Trinity Methodist Church",
        "tag_name": "trinity_methodist_church",
        "category_id": 1,
        "category_text": "Notable Buildings",
        "category_name": "notable_buildings",
        "colour": "eeeeff"
      },
      {
        "tag_id": 220,
        "tag_text": "Irving Terrace",
        "tag_name": "irving_terrace",
        "category_id": 3,
        "category_text": "Location",
        "category_name": "location",
        "colour": "eeffee"
      },
      {
        "tag_id": 226,
        "tag_text": "people",
        "tag_name": "people",
        "category_id": 4,
        "category_text": "Miscellaneous",
        "category_name": "misc",
        "colour": "eeeeee"
      },
      {
        "tag_id": 256,
        "tag_text": "sea",
        "tag_name": "sea",
        "category_id": 4,
        "category_text": "Miscellaneous",
        "category_name": "misc",
        "colour": "eeeeee"
      },
      {
        "tag_id": 485,
        "tag_text": "omnibus",
        "tag_name": "omnibus",
        "category_id": 4,
        "category_text": "Miscellaneous",
        "category_name": "misc",
        "colour": "eeeeee"
      },
      {
        "tag_id": 536,
        "tag_text": "West End bandstand",
        "tag_name": "west_end_bandstand",
        "category_id": 1,
        "category_text": "Notable Buildings",
        "category_name": "notable_buildings",
        "colour": "eeeeff"
      },
      {
        "tag_id": 595,
        "tag_text": "Horse-drawn tram",
        "tag_name": "horse-drawn_tram",
        "category_id": 4,
        "category_text": "Miscellaneous",
        "category_name": "misc",
        "colour": "eeeeee"
      },
      {
        "tag_id": 605,
        "tag_text": "West Street",
        "tag_name": "west_street",
        "category_id": 3,
        "category_text": "Location",
        "category_name": "location",
        "colour": "eeffee"
      },
      {
        "tag_id": 672,
        "tag_text": "Morecambe",
        "tag_name": "morecambe",
        "category_id": 7,
        "category_text": "Primary Area",
        "category_name": "primary_area",
        "colour": "ffeeee"
      },
      {
        "tag_id": 697,
        "tag_text": "charabanc",
        "tag_name": "charabanc",
        "category_id": 4,
        "category_text": "Miscellaneous",
        "category_name": "misc",
        "colour": "eeeeee"
      }
    ],
    "publication_description": "Millar & Lang, Art Publishing Co. Ltd, 'National' series",
    "index": 88,
    "images": {
      "front": {
        "id": 88,
        "alt": "An extremely quiet image in which even the flowerbeds of the West End appear somewhat dull and devoid of the elaborate planting that marked later periods, as they comprise mainly grass covered lawns with flowerbeds arranged around the edge. A few, mainly solitary, people are walking along the promenade, though a few are walking in pairs or small groups with three or four companions. Both the horse-drawn trams - a open-topped, single deck 'toast rack' tram heading towards Heysham, as well as an open-topped double decker about to start its journey to Bare - appear largely deserted, with no passengers being visible on the open decks.  "
      },
      "rear": {
        "id": 88,
        "alt": ""
      },
      "thumb": {
        "id": 88,
        "alt": "Promenade and Gardens, Morecambe W."
      }
    },
    "recipient": {
      "name": "Miss Ball",
      "address": "2 Preston Street, off Manchester Road, Bolton",
      "location": null
    },
    "stamps": [
      {
        "id": 4,
        "description": "George V 1 1/2d (brown)",
        "condition": "intact"
      }
    ],
    "posted_date": {
      "year": 1919,
      "month": 8,
      "day": 15,
      "date": "1919-08-15T00:00:00Z",
      "approximate": false
    },
    // "publisher_id": null,
    "publisher": {
      "id": 1,
      "name": "Millar & Lang, Art Publishing Co. Ltd"
    },
    // "series_id": null,
    "series": {
      "id": 1,
      "name": "'National' series"
    },
    "id": 88,
    "notes": "This view of Marine Road West, encompassing Alexandra Road to Highfield Crescent, was taken from an elevated position, probably in the Battery Hotel. \u003Ca href=\"/api/notes/31\"\u003ETrinity Methodist Church\u003C/a\u003E, the \u003Ca href=\"/api/notes/29\"\u003EAlhambra Palace Theatre\u003C/a\u003E and the West End bandstand can all be seen.",
    "flags": {
      "draft": false,
      "rp": false,
      "used": true,
      "posted": true,
      "franked": true,
      "divided_back": true
    },
    "subject": {
      "description": "Promenade and Gardens, Morecambe W.",
      "location": {
        "lat": 54.064875,
        "lng": -2.884958
      },
      "current_view": "https://www.google.com/maps/embed?pb=!1m0!3m2!1sen!2suk!4v1437231588024!6m8!1m7!1ssitIMA_7UNFZr2gOCoPBrg!2m2!1d54.064875!2d-2.884958!3f63.572019308839074!4f5.737706841473994!5f1.48784743184645"
    },
    "publication_date": {
      "year": 1915,
      "month": null,
      "day": null,
      "date": "1915-01-01T00:00:00Z",
      "approximate": true
    }
  };

  return (
    <div>
      <div>Home Page</div>
      <CardForm card={undefined} />
      {/* <div>Authenticated: {isAuthenticated ? "true" : "false"}</div>
      <div>User: {user?.name}</div>
      <div>Pathname: {pathname}</div> */}
      {/* <TagForm />
      <PublisherForm />
      <SeriesForm />
      <TagCategoryForm />
      <FormExample /> */}
      {/* <PaginationExample /> */}
      {/* <ApiCall /> */}
    </div>
  );
}