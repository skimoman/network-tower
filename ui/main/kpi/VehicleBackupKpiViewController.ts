// Copyright 2015-2019 SWIM.AI inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import {NodeRef} from "@swim/client";
import {Color} from "@swim/color";
import {Transition} from "@swim/transition";
import {MapGraphicView} from "@swim/map";
import {SignalPhase} from "../map/IntersectionModel";
import {IntersectionMapView} from "../map/IntersectionMapView";
import {ApproachMapView} from "../map/ApproachMapView";
import {TrafficKpiViewController} from "./TrafficKpiViewController";

export class VehicleBackupKpiViewController extends TrafficKpiViewController {
  /** @hidden */
  _nodeRef: NodeRef;
  /** @hidden */
  _trafficMapView: MapGraphicView;

  constructor(nodeRef: NodeRef, trafficMapView: MapGraphicView) {
    super();
    this._nodeRef = nodeRef;
    this._trafficMapView = trafficMapView;
  }

  get primaryColor(): Color {
    return Color.parse("#EF84DB");
  }

  updateKpi(): void {
    let meterValue = Math.floor(Math.random() * 99) + 1;
    let spaceValue = Math.floor(Math.random() * 99) + 1;
    const intersectionMapViews = this._trafficMapView.childViews;
    for (let i = 0; i < intersectionMapViews.length; i += 1) {
      const intersectionMapView = intersectionMapViews[i];
      if (intersectionMapView instanceof IntersectionMapView && !intersectionMapView.culled) {
        const approachMapViews = intersectionMapView.childViews;
        for (let j = 0; j < approachMapViews.length; j += 1) {
          const approachMapView = approachMapViews[j];
          if (approachMapView instanceof ApproachMapView) {
            const approachMapViewController = approachMapView.viewController!;
            if (approachMapViewController._phase === SignalPhase.Red) {
              if (approachMapViewController._occupied) {
                meterValue += 1;
              } else {
                spaceValue += 1;
              }
            }
          }
        }
      }
    }

    const title = this.titleView;
    const meter = this.meterView;
    const empty = this.emptyView;
    const tween = Transition.duration<any>(1000);

    this.title!.text('East Palo Alto - Dropped Packets');
    this.subtitle!.text('@ Network Towers');

    meter.value(meterValue, tween);
    empty.value(spaceValue, tween);
    this.meterLegend!.text("Dropped (" + meterValue + ")");
    this.clearLegend!.text("Clear (" + spaceValue + ")");
    title.text(Math.round(100 * meterValue / ((meterValue + spaceValue) || 1)) + "%");
  }
}
