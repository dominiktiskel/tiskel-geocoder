import { IPoi } from 'app/entities/poi/poi.model';

export interface ISource {
  id?: number;
  name?: string;
  active?: boolean;
  pois?: IPoi[] | null;
}

export class Source implements ISource {
  constructor(public id?: number, public name?: string, public active?: boolean, public pois?: IPoi[] | null) {
    this.active = this.active ?? false;
  }
}

export function getSourceIdentifier(source: ISource): number | undefined {
  return source.id;
}
