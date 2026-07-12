import { buildQueryParams } from './build-query-params';

describe('buildQueryParams', () => {
  it('deve montar os parametros basicos de paginacao', () => {
    const result = buildQueryParams({ page: 0, size: 10 });
    expect(result['page']).toBe(0);
    expect(result['size']).toBe(10);
  });

  it('deve incluir o sort quando informado', () => {
    const result = buildQueryParams({ page: 0, size: 10, sort: 'titulo,asc' });
    expect(result['sort']).toBe('titulo,asc');
  });

  it('deve incluir os filtros quando informados', () => {
    const result = buildQueryParams(
      { page: 0, size: 10 },
      { status: 'ABERTA', prioridade: 'ALTA', q: 'erro' }
    );
    expect(result['status']).toBe('ABERTA');
    expect(result['prioridade']).toBe('ALTA');
    expect(result['q']).toBe('erro');
  });

  it('deve aceitar filtros vazios sem quebrar', () => {
    const result = buildQueryParams({ page: 1, size: 20 });
    expect(result['status']).toBeUndefined();
    expect(result['prioridade']).toBeUndefined();
    expect(result['q']).toBeUndefined();
  });
});