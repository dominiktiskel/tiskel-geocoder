import { ISource } from 'app/entities/source/source.model';
import { IPoiGroup } from 'app/entities/poi-group/poi-group.model';

export interface IPoi {
  id?: number;
  name?: string;
  street?: string | null;
  buildingNumber?: string | null;
  postCode?: string | null;
  lat?: number;
  lng?: number;
  active?: boolean;
  source?: ISource;
  poiGroup?: IPoiGroup;
}

export class Poi implements IPoi {
  constructor(
    public id?: number,
    public name?: string,
    public street?: string | null,
    public buildingNumber?: string | null,
    public postCode?: string | null,
    public lat?: number,
    public lng?: number,
    public active?: boolean,
    public source?: ISource,
    public poiGroup?: IPoiGroup
  ) {
    this.active = this.active ?? false;
  }
}

export function getPoiIdentifier(poi: IPoi): number | undefined {
  return poi.id;
}
