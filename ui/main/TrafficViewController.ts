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

import * as mapboxgl from "mapbox-gl";
import {NodeRef} from "@swim/client";
import {Color} from "@swim/color";
import {/*SvgView, */HtmlView, HtmlViewController} from "@swim/view";
import {MapboxView} from "@swim/mapbox";
import {TrafficMapView} from "./map/TrafficMapView";
import {TrafficMapViewController} from "./map/TrafficMapViewController";
import {VehicleFlowKpiViewController} from "./kpi/VehicleFlowKpiViewController";
//import {VehicleBackupKpiViewController} from "./kpi/VehicleBackupKpiViewController";
import {PedestrianBackupKpiViewController} from "./kpi/PedestrianBackupKpiViewController";
import {ProspectiveDevicesKpiViewController} from "./kpi/ProspectiveDevicesKpiViewController";
import {ActiveSessionsKpiViewController} from "./kpi/ActiveSessionsKpiViewController";

export class TrafficViewController extends HtmlViewController {
  /** @hidden */
  _nodeRef: NodeRef;
  /** @hidden */
  _map: mapboxgl.Map | null;

  constructor(nodeRef: NodeRef) {
    super();
    this._nodeRef = nodeRef;
    this._map = null;
  }

  didSetView(view: HtmlView): void {
    this._map = new mapboxgl.Map({
      container: view.node,
      style: "mapbox://styles/swimit/cjs5h20wh0fyf1gocidkpmcvm",
      center: {lng: -122.131562, lat: 37.461943}, 
      pitch: 70,
      zoom: 15.5,
    });

    const mapboxView = new MapboxView(this._map);
    mapboxView.overlayCanvas();

    const trafficMapView = new TrafficMapView();
    const trafficMapViewController = new TrafficMapViewController(this._nodeRef);
    trafficMapView.setViewController(trafficMapViewController);
    mapboxView.setChildView("map", trafficMapView);

    // const header = view.append("div")
        // .key("header")
        // .pointerEvents("none")
        // .zIndex(10);

    // const logo = header.append("div")
        // .key("logo")
        // .position("absolute")
        // .left(8)
        // .top(8)
        // .width(156)
        // .height(68);
    // logo.append(this.createLogo());

    view.append(this.createKpiStack(trafficMapView));
	view.append(this.createLeftStack(trafficMapView));
    this.layoutKpiStack();
	this.layoutLeftStack();
  }

  viewDidResize(): void {
    this.layoutKpiStack();
	this.layoutLeftStack();
  }

  protected createKpiStack(trafficMapView: TrafficMapView): HtmlView {
    const kpiStack = HtmlView.fromTag("div")
        .key("kpiStack")
        .position("absolute")
        .right(0)
        .top(0)
        .bottom(0)
        .zIndex(9)
        .pointerEvents("none");

    const vehicleFlowKpi = kpiStack.append("div")
        .key("vehicleFlowKpi")
        .position("absolute")
        .borderRadius(8)
        .boxSizing("border-box")
        .backgroundColor(Color.parse("#070813").alpha(0.33))
        .backdropFilter("blur(2px)")
        .pointerEvents("auto");
    const vehicleFlowKpiViewController = new VehicleFlowKpiViewController(this._nodeRef, trafficMapView);
    vehicleFlowKpi.setViewController(vehicleFlowKpiViewController);
	
		const activeSessionsKpi = kpiStack.append("div")
        .key("activeSessionsKpi")
        .position("absolute")
        .borderRadius(8)
        .boxSizing("border-box")
        .backgroundColor(Color.parse("#070813").alpha(0.33))
        .backdropFilter("blur(2px)")
        .pointerEvents("auto");
    const activeSessionsKpiViewController = new ActiveSessionsKpiViewController(this._nodeRef, trafficMapView);
    activeSessionsKpi.setViewController(activeSessionsKpiViewController);

    // const vehicleBackupKpi = kpiStack.append("div")
        // .key("vehicleBackupKpi")
        // .position("absolute")
        // .borderRadius(8)
        // .boxSizing("border-box")
        // .backgroundColor(Color.parse("#070813").alpha(0.33))
        // .backdropFilter("blur(2px)")
        // .pointerEvents("auto");
    // const vehicleBackupKpiViewController = new VehicleBackupKpiViewController(this._nodeRef, trafficMapView);
    // vehicleBackupKpi.setViewController(vehicleBackupKpiViewController);

    const pedestrianBackupKpi = kpiStack.append("div")
        .key("pedestrianBackupKpi")
        .position("absolute")
        .borderRadius(8)
        .boxSizing("border-box")
        .backgroundColor(Color.parse("#070813").alpha(0.33))
        .backdropFilter("blur(2px)")
        .pointerEvents("auto");
    const pedestrianBackupKpiViewController = new PedestrianBackupKpiViewController(this._nodeRef, trafficMapView);
    pedestrianBackupKpi.setViewController(pedestrianBackupKpiViewController);
	
	const prospectiveDevicesKpi = kpiStack.append("div")
        .key("prospectiveDevicesKpi")
        .position("absolute")
        .borderRadius(8)
        .boxSizing("border-box")
        .backgroundColor(Color.parse("#070813").alpha(0.33))
        .backdropFilter("blur(2px)")
        .pointerEvents("auto");
    const prospectiveDevicesKpiViewController = new ProspectiveDevicesKpiViewController(this._nodeRef, trafficMapView);
    prospectiveDevicesKpi.setViewController(prospectiveDevicesKpiViewController);
	
    return kpiStack;
	
  }
  
   protected createLeftStack(trafficMapView: TrafficMapView): HtmlView {
	   const leftStack = HtmlView.fromTag("div")
        .key("leftStack")
        .position("absolute")
        .left(0)
        .top(300)
        .bottom(0)
        .zIndex(9)
        .pointerEvents("none");
		
	   
	
	return leftStack;
	
   }

  protected layoutKpiStack(): void {
    const kpiMargin = 16;

    const view = this._view!;
    const kpiStack = view.getChildView("kpiStack") as HtmlView;

    const kpiViews = kpiStack.childViews;
    const kpiViewCount = kpiViews.length;
    const kpiViewHeight = (view.node.offsetHeight - kpiMargin * (kpiViewCount + 1)) / (kpiViewCount || 1);
    const kpiViewWidth = 1.5 * kpiViewHeight;

    const kpiStackWidth = kpiViewWidth + 2 * kpiMargin;
    kpiStack.width(kpiStackWidth);
    for (let i = 0; i < kpiViewCount; i += 1) {
      const kpiView = kpiViews[i] as HtmlView;
      kpiView.right(kpiMargin)
             .top(kpiViewHeight * i + kpiMargin * (i + 1))
             .width(kpiViewWidth)
             .height(kpiViewHeight);
    }

    if (kpiStackWidth > 240 && view.node.offsetWidth >= 2 * kpiStackWidth) {
      kpiStack.display("block");
    } else {
      kpiStack.display("none");
    }
  }
  
  protected layoutLeftStack(): void {
	const leftMargin = 16;
	
	const view = this._view!;
    const leftStack = view.getChildView("leftStack") as HtmlView;

    const leftViews = leftStack.childViews;
    const leftViewCount = leftViews.length;
    const leftViewHeight = (view.node.offsetHeight - leftMargin * (leftViewCount + 1)) / (leftViewCount || 1);
    const leftViewWidth = 1.5 * leftViewHeight;

    const leftStackWidth = leftViewWidth + 2 * leftMargin;
    leftStack.width(leftStackWidth);
    for (let i = 0; i < leftViewCount; i += 1) {
      const leftView = leftViews[i] as HtmlView;
      leftView.left(leftMargin)
             .top(leftViewHeight * i + leftMargin * (i + 1))
             .width(leftViewWidth)
             .height(leftViewHeight);
    }

    if (leftStackWidth > 240 && view.node.offsetWidth >= 2 * leftStackWidth) {
      leftStack.display("block");
    } else {
      leftStack.display("none");
    }
  }
}
