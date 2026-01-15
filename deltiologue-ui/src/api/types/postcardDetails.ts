
export interface Location {
  lat: number;
  lng: number;
}

export interface PostcardDetails {
  collectionIndex: string;
  posted: boolean;
  recipient_address: string;
  image_front: number;
  rp: boolean;
  recipient_location: string;
  image_thumb: number;
  divided_back: boolean;
  publication_day: number;
  publisher: number;
  posted_year: number;
  series: number;
  series_entry: string;
  recipient: number;
  recipient_name: string;
  image_rear_alt: string;
  publication_date_approximate: boolean;
  posted_date: string;
  subject_current_view: string;
  posted_date_approximate: boolean;
  image_thumb_alt: string;
  subject_location: Location;
  publication_month: number;
  notes: string;
  posted_month: number;
  franked: boolean;
  publisher_name: string;
  subject_description: string;
  transcript: string;
  publication_year: number;
  posted_day: number;
  image_rear: number;
  series_name: string;
  image_front_alt: string;
  publication_date: string;
  used: boolean;
}