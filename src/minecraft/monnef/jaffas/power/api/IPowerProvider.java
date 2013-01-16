package monnef.jaffas.power.api;

// classic: generator -> toaster
// [ provider -> ] [ -> consumer ]

// distribution: (generator) -> {antenna} -> /toaster\
// ([ provider -> ]) {[ -> consumer ] [ provider -> ]} ... /[ -> consumer ]\

public interface IPowerProvider {
    IPowerProviderManager getPowerManager();
}

