################################################################################
# Automatically-generated file. Do not edit!
################################################################################

-include ../makefile.init

RM := rm -rf

# All of the sources participating in the build are defined here
-include sources.mk
-include trunk/src/server/subdir.mk
-include trunk/src/network/subdir.mk
-include trunk/src/lib/subdir.mk
-include trunk/src/cubist/subdir.mk
-include trunk/src/client/subdir.mk
-include trunk/src/subdir.mk
-include .metadata/.plugins/org.eclipse.cdt.make.core/subdir.mk
-include subdir.mk
-include objects.mk

ifneq ($(MAKECMDGOALS),clean)
ifneq ($(strip $(C_DEPS)),)
-include $(C_DEPS)
endif
endif

-include ../makefile.defs

# Add inputs and outputs from these tool invocations to the build variables 

# All Target
all: DAGSwithCubist

# Tool invocations
DAGSwithCubist: $(OBJS) $(USER_OBJS)
	@echo 'Building target: $@'
	@echo 'Invoking: Cross GCC Linker'
	gcc -pg -o "DAGSwithCubist" $(OBJS) $(USER_OBJS) $(LIBS)
	@echo 'Finished building target: $@'
	@echo ' '

# Other Targets
clean:
	-$(RM) $(OBJS)$(C_DEPS)$(EXECUTABLES) DAGSwithCubist
	-@echo ' '

.PHONY: all clean dependents
.SECONDARY:

-include ../makefile.targets
