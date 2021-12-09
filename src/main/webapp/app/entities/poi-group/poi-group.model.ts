import { IPoi } from 'app/entities/poi/poi.model';

export interface IPoiGroup {
  id?: number;
  name?: string;
  prefix?: string;
  active?: boolean;
  pois?: IPoi[] | null;
}

export class PoiGroup implements IPoiGroup {
  constructor(public id?: number, public name?: string, public prefix?: string, public active?: boolean, public pois?: IPoi[] | null) {
    this.active = this.active ?? false;
  }
}

export function getPoiGroupIdentifier(poiGroup: IPoiGroup): number | undefined {
  return poiGroup.id;
}
