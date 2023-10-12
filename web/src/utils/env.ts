let env = {
  BASE_URL: "",
};

export function getEnvByKey(key: keyof typeof env) {
  return env[key];
}
export function configEnv(partialEnv: Partial<typeof env>) {
  env = {
    ...env,
    ...partialEnv,
  };
}
