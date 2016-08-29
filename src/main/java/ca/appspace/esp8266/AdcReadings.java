/*******************************************************************************
 * Copyright (c) ecobee, Inc. 2016. All rights reserved.
 *******************************************************************************/
package ca.appspace.esp8266;

import java.io.Serializable;
import java.util.Arrays;

public class AdcReadings implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5902621527694142068L;

	private Integer[] values;
	private String chipId;
	private String bootMode;
	private String bootVersion;
	private String coreVersion;
	private String cpuFreq;
	private String flashChipId;
	private String flashChipMode;
	private String flashRealSize;
	private String espSdkVersion;
	private String espResetReason;

	public Integer[] getValues() {
		return values;
	}
	public void setValues(Integer[] values) {
		this.values = values;
	}

	public String getChipId() {
		return chipId;
	}
	public void setChipId(String chipId) {
		this.chipId = chipId;
	}
	public String getBootMode() {
		return bootMode;
	}
	public void setBootMode(String bootMode) {
		this.bootMode = bootMode;
	}
	public String getBootVersion() {
		return bootVersion;
	}
	public void setBootVersion(String bootVersion) {
		this.bootVersion = bootVersion;
	}
	public String getCoreVersion() {
		return coreVersion;
	}
	public void setCoreVersion(String coreVersion) {
		this.coreVersion = coreVersion;
	}
	public String getCpuFreq() {
		return cpuFreq;
	}
	public void setCpuFreq(String cpuFreq) {
		this.cpuFreq = cpuFreq;
	}
	public String getFlashChipId() {
		return flashChipId;
	}
	public void setFlashChipId(String flashChipId) {
		this.flashChipId = flashChipId;
	}
	public String getFlashChipMode() {
		return flashChipMode;
	}
	public void setFlashChipMode(String flashChipMode) {
		this.flashChipMode = flashChipMode;
	}
	public String getFlashRealSize() {
		return flashRealSize;
	}
	public void setFlashRealSize(String flashRealSize) {
		this.flashRealSize = flashRealSize;
	}
	public String getEspSdkVersion() {
		return espSdkVersion;
	}
	public void setEspSdkVersion(String espSdkVersion) {
		this.espSdkVersion = espSdkVersion;
	}
	public String getEspResetReason() {
		return espResetReason;
	}
	public void setEspResetReason(String espResetReason) {
		this.espResetReason = espResetReason;
	}
	@Override
	public String toString() {
		return "AdcReadings " + Arrays.toString(values);
	}

}
