#
# Copyright (C) 2023-2024 The LineageOS Project
#
# SPDX-License-Identifier: Apache-2.0
#

# Check for target product
ifeq (aospa_stone,$(TARGET_PRODUCT))

# ART Userfaultfd Garbage Collection
PRODUCT_ENABLE_UFFD_GC := true

# Inherit from those products. Most specific first.
$(call inherit-product, $(SRC_TARGET_DIR)/product/core_64_bit_only.mk)
$(call inherit-product, $(SRC_TARGET_DIR)/product/aosp_base_telephony.mk)

# Inherit from device.
$(call inherit-product, $(LOCAL_PATH)/device.mk)

# Disable full value-adds framework manifest before inheriting common AOSPA
TARGET_FWK_SUPPORTS_FULL_VALUEADDS := false

# Inherit common AOSPA configuration.
$(call inherit-product, vendor/aospa/target/product/aospa-target.mk)


# Exclude framework manifest intended for full value-adds (prevents qccsyshal/atcmdfwd/systemhelper missing service spam)
DEVICE_FRAMEWORK_MANIFEST_FILE := $(filter-out device/qcom/qssi_64/framework_manifest.xml,$(DEVICE_FRAMEWORK_MANIFEST_FILE))

# Boot animation
TARGET_BOOT_ANIMATION_RES := 1080

# Device identifier
PRODUCT_NAME := aospa_stone
PRODUCT_DEVICE := stone
PRODUCT_BRAND := Redmi
PRODUCT_MANUFACTURER := Xiaomi
PRODUCT_MODEL := Redmi Note 12 5G

PRODUCT_GMS_CLIENTID_BASE := android-xiaomi

PRODUCT_BUILD_PROP_OVERRIDES += \
    BuildDesc="sunstone_global-user 14 UKQ1.240624.001 OS2.0.5.0.UMQMIXM release-keys" \
    BuildFingerprint=Redmi/sunstone_global/sunstone:14/UKQ1.240624.001/OS2.0.5.0.UMQMIXM:user/release-keys \
    DeviceProduct=sunstone

endif

